package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tomas.passwordmanager.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ItemList extends Fragment {

    SQLiteDatabase myDatabase;
    ArrayAdapter<String> arrayAdapter;
    ListView myListView;
    int count = 0;



    ItemListListener activityCommander;

    public interface ItemListListener{
        public void addItem();
    }

    @Override
    public void onResume() {
        super.onResume();

        getDataFromDatabase();
        if(count >0) myListView.setAdapter(arrayAdapter);
        count++;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCommander = (ItemListListener) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_lists, container, false);

        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.addButton);


        try {

            myDatabase = getActivity().openOrCreateDatabase("PasswordManager", MODE_PRIVATE, null);
            //myDatabase.execSQL("DROP TABLE passwords");
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS passwords (ID integer UNIQUE, site VARCHAR, username VARCHAR, password VARCHAR)");

            //myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (0, 'Facebook123', 'tomas', 'slaptazodis')");
            try {
                //myDatabase.execSQL("INSERT INTO passwords (ID, site, username, password) VALUES (1, 'Instagram', 'tomas', 'slaptazodis123')");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //FETCH DATA TO ADAPTER
            getDataFromDatabase();

            myListView = (ListView) view.findViewById(R.id.myListView);
            myListView.setAdapter(arrayAdapter);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), NewItemActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("function", "Edit");
                    startActivity(intent);

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        FloatingActionButton addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        addNewItem(v);
                    }
                }
        );
        return view;
    }
    public void addNewItem(View view) {
        activityCommander.addItem();
    }

    public void getDataFromDatabase(){
        try {
            ArrayList<String> sites = new ArrayList<String>();

            Cursor c = myDatabase.rawQuery("SELECT * FROM passwords", null);
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
                sites.add(c.getString(siteIndex) + "\nUsername: " + c.getString(usernameIndex) + "\nPassword: " + c.getString(passwordIndex));

                c.moveToNext();

            }
            arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sites);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
