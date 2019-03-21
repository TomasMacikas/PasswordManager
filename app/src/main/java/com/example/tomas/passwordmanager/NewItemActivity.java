package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.tomas.passwordmanager.R;

import java.util.Random;

public class NewItemActivity extends AppCompatActivity {

    EditText site;
    EditText username;
    EditText password;
    SQLiteDatabase myDatabase;
    Intent intent;
    int itemID = -1;

    SharedPreferences sharedPreferences;

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!sharedPreferences.getBoolean("add", false)){
            finish();
            myDatabase.close();
        }else{
            if(!sharedPreferences.getBoolean("back", true)){
                sharedPreferences.edit().putBoolean("add", false).apply();
                finish();
            }
            //HE BACKED OR main pressed
        }
        //finish();
        //sharedPreferences.edit().putBoolean("add", false).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!sharedPreferences.getBoolean("back", true)){
            sharedPreferences.edit().putBoolean("add", false).apply();
            sharedPreferences.edit().putBoolean("back", false).apply();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);

        site = (EditText) findViewById(R.id.websiteEditText);
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

        //establish connection database
        myDatabase = this.openOrCreateDatabase("PasswordManager", MODE_PRIVATE, null);
        //Get data from last activity
        intent = getIntent();
        String function = intent.getStringExtra("function");
        if(function.equals("Edit")){
            //Edit EditTexts
            getDataFromDatabase();
        }

    }

    public void getDataFromDatabase(){
        int ID = intent.getIntExtra("position", -1);

        Cursor c = myDatabase.rawQuery("SELECT * FROM passwords ORDER BY site", null);
        int IDIndex = c.getColumnIndex("ID");
        int siteIndex = c.getColumnIndex("site");
        int usernameIndex = c.getColumnIndex("username");
        int passwordIndex = c.getColumnIndex("password");

        c.moveToFirst();

        for(int k = 0; k<ID; ++k)c.moveToNext();

        if(ID != -1){
            itemID = Integer.parseInt(c.getString(IDIndex));
            site.setText(c.getString(siteIndex));
            username.setText(c.getString(usernameIndex));
            password.setText(Encryption.decrypt(c.getString(passwordIndex)));
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelete:
                if(itemID>=0){
                    try {
                        myDatabase.execSQL("DELETE FROM passwords WHERE ID=" + Integer.toString(itemID));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void createNew(View view){
        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        intent.putExtra("password", "Add");
        startActivityForResult(intent, 0);
        sharedPreferences.edit().putBoolean("back", true).apply();
    }

    public void takePhoto(View view){
        Intent intent = new Intent(getApplicationContext(), TextDetector.class);
        intent.putExtra("password", "Add");
        startActivityForResult(intent, 0);
        sharedPreferences.edit().putBoolean("back", true).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String passwordString = "";
        if(resultCode == 1) {
            passwordString = data.getStringExtra("password");
            password.setText(passwordString);
        }
    }
    public void doneClicked(View view){
        String siteString = site.getText().toString();
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        while(true) {
            try {
                Random rand = new Random();
                String number = Integer.toString(rand.nextInt(50) + 1);
                myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES ('" + number +"','" +
                        siteString + "' , '" + usernameString + "' , '" + Encryption.encrypt(passwordString) + "')");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(itemID>=0){
            try {
                myDatabase.execSQL("DELETE FROM passwords WHERE ID=" + Integer.toString(itemID));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        finish();
    }
}
