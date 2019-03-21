package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tomas.passwordmanager.R;

public class FirstTimeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);
        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);


    }

    public void fourDigits(View view){
        //sharedPreferences.edit().putString("type", "fourDigits").apply();
        sharedPreferences.edit().putBoolean("firstTime", true);
        //Log.i("preferences", sharedPreferences.getString("type", ""));
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void custom(View view){
        sharedPreferences.edit().putBoolean("firstTime", true);
        //sharedPreferences.edit().putString("type", "custom").apply();
        Intent intent = new Intent(getApplicationContext(), CustomLogin.class);
        startActivity(intent);
        finish();
    }
}
