package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tomas.passwordmanager.R;

import java.util.ArrayList;
import java.util.Random;

public class ItemListActivity extends AppCompatActivity implements ItemList.ItemListListener{

    SQLiteDatabase myDatabase;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<NewItem> results;
    ListView myListView;
    Intent intent;
    int count = 0;

    SharedPreferences sharedPreferences;
    @Override
    public void onResume() {
        super.onResume();

        getDataFromDatabase();
        //if(count >0) myListView.setAdapter(arrayAdapter);
        if(count >0) myListView.setAdapter(new CustomListAdapter(this, results));

        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("change password", false).apply();
        sharedPreferences.edit().putBoolean("add", false).apply();
        count++;

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(!sharedPreferences.getBoolean("add", false)) {
            if ((sharedPreferences.getString("type", "")).equals("custom")) {
                intent = new Intent(getApplicationContext(), CustomLogin.class);
            } else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            sharedPreferences.edit().putBoolean("add", false).apply();
            intent.putExtra("function", "Add");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SharedPreferences sharedPreferences;
                sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("change password", true).apply();
                sharedPreferences.edit().putBoolean("add", true).apply();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            case R.id.action_rate:
                intent = new Intent(getApplicationContext(), DrawNumberActivity.class);
                startActivity(intent);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_lists);


        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.addButton);


        try {

            myDatabase = this.openOrCreateDatabase("PasswordManager", MODE_PRIVATE, null);
            //myDatabase.execSQL("DROP TABLE passwords");
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS passwords (ID integer UNIQUE, site VARCHAR, username VARCHAR, password VARCHAR)");

            //addTestData();

            //FETCH DATA TO ADAPTER
            getDataFromDatabase();

            myListView = (ListView) findViewById(R.id.myListView);

            myListView.setAdapter(new CustomListAdapter(this, results));

            //myListView.setAdapter(arrayAdapter);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), NewItemActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("function", "Edit");
                    startActivity(intent);
                    sharedPreferences.edit().putBoolean("add", true).apply();
                    sharedPreferences.edit().putBoolean("back", false).apply();

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        FloatingActionButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        addNewItem(v);
                    }
                }
        );



/*
        itemListFragment = new ItemList();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.firstLayout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        */

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();

            }
        }
    }

    public void addItem() {
        Intent intent = new Intent(getApplicationContext(), NewItemActivity.class);
        intent.putExtra("function", "Add");
        startActivity(intent);
        sharedPreferences.edit().putBoolean("add", true).apply();
        sharedPreferences.edit().putBoolean("back", false).apply();

    }
    public void addNewItem(View view){
        addItem();
    }

    public void getDataFromDatabase(){
        try {
            ArrayList<String> sites = new ArrayList<String>();

            results = new ArrayList<NewItem>();

            Cursor c = myDatabase.rawQuery("SELECT * FROM passwords ORDER BY site", null);
            int IDIndex = c.getColumnIndex("ID");
            int siteIndex = c.getColumnIndex("site");
            int usernameIndex = c.getColumnIndex("username");
            int passwordIndex = c.getColumnIndex("password");

            c.moveToFirst();

            while (!c.isAfterLast()) {
                /*
                Log.i("ID", c.getString(IDIndex));
                Log.i("site", c.getString(siteIndex));
                Log.i("username", c.getString(usernameIndex));
                Log.i("password", c.getString(passwordIndex));
*/
                NewItem newData = new NewItem();
                newData.setSite(c.getString(siteIndex));
                newData.setUsername(c.getString(usernameIndex));
                newData.setPassword(Encryption.decrypt(c.getString(passwordIndex)));
                results.add(newData);


                sites.add(c.getString(siteIndex) + "\nUsername: " + c.getString(usernameIndex) + "\nPassword: " + c.getString(passwordIndex));
                c.moveToNext();
            }
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sites);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addTestData(){
        try {
            Encryption encryption = new Encryption();
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (0, 'Facebook', 'Tomas322', '" + encryption.encrypt("Slaptazodis322") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (1, 'Instagram', 'Jonas51', '" + encryption.encrypt("123456789") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (2, 'Gmail', 'Macikastomas', '" + encryption.encrypt("tomas22") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (3, 'Amazon', 'tomas1234', '" + encryption.encrypt("tom322as") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (4, 'Booking.com', 'Tomas322', '" + encryption.encrypt("sausainis") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (5, 'Discord', 'Tomas322', '" + encryption.encrypt("Televizorius32") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (6, 'Gearbest', 'Biscuit322', '" + encryption.encrypt("1597532486") + "')");
            myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (7, 'Trello', 'macikastomas@gmail.com', '" + encryption.encrypt("trelloslpt") + "')");;
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
