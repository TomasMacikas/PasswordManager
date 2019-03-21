package com.example.tomas.passwordmanager;




import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
        //startActivity(intent);

        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);

        //sharedPreferences.edit().putBoolean("firstTime", true).apply();
        if(sharedPreferences.getBoolean("firstTime", true)) {
            //Intent intent = new Intent(getApplicationContext(), FirstTimeActivity.class);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        }
        else{
            Intent intent;
            if(sharedPreferences.getString("type", "fourDigits").equals("custom")) {
                intent = new Intent(getApplicationContext(), CustomLogin.class);

            }else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            startActivity(intent);
            finish();

        }
    }

}
