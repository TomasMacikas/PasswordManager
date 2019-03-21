package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tomas.passwordmanager.R;

public class CustomLogin extends AppCompatActivity {

    EditText password;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);
        password = findViewById(R.id.passwordEditText);
        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);

    }

    public void passwordEntered(View view){
        if(sharedPreferences.getBoolean("firstTime", true) || sharedPreferences.getBoolean("change password", true)){
            sharedPreferences.edit().putBoolean("firstTime", false).apply();

            //sharedPreferences.edit().putString("password", entered).apply();
            sharedPreferences.edit().putString("password", password.getText().toString()).apply();
            //Log.i("number", sharedPreferences.getString("password", ""));
            sharedPreferences.edit().putString("type", "custom").apply();
            if(sharedPreferences.getBoolean("change password", false)){
                sharedPreferences.edit().putBoolean("change password", false).apply();
                finish();
                return;
            }
        }

        if(password.getText().toString().equals(sharedPreferences.getString("password", ""))){
            goToItemList();
            finish();
        }
        else{
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_LONG).show();
        }
    }
    private void goToItemList(){
        Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
        startActivity(intent);
    }

}
