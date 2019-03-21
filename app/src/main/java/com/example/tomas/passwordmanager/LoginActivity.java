package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tomas.passwordmanager.R;

public class LoginActivity extends AppCompatActivity {
    String password = "1111";
    String entered = "";

    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

    }

    public void numberClicked(View view){
        Log.i("number clicked", view.getTag().toString());

        entered += view.getTag().toString();

        if(entered.length() == 4){
            if(sharedPreferences.getBoolean("firstTime", true) || sharedPreferences.getBoolean("change password", true)){
                sharedPreferences.edit().putBoolean("firstTime", false).apply();
                //sharedPreferences.edit().putString("password", entered).apply();
                sharedPreferences.edit().putString("password", entered).apply();
                sharedPreferences.edit().putString("type", "fourDigits").apply();
                Log.i("number", sharedPreferences.getString("password", ""));
                if(sharedPreferences.getBoolean("change password", false)){
                    sharedPreferences.edit().putBoolean("change password", false).apply();
                    finish();
                    return;
                }
            }
            if(entered.equals(sharedPreferences.getString("password", ""))){

                goToItemList();
                Log.i("number", "CORRECT!!!");
                //finish();

            }
            else{
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_LONG).show();
            }
        }
        else if(entered.length() > 4){
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            entered = "";
            entered += view.getTag().toString();
        }
        if(entered.length() == 1)radioButton1.setChecked(true);
        if(entered.length() == 2)radioButton2.setChecked(true);
        if(entered.length() == 3)radioButton3.setChecked(true);
        if(entered.length() == 4)radioButton4.setChecked(true);
    }
    private void goToItemList(){
        Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
        startActivity(intent);
    }
}
