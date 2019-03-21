package com.example.tomas.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tomas.passwordmanager.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class PasswordActivity extends AppCompatActivity {
    Switch capital;
    Switch number;
    Switch special;
    EditText size;
    CheckBox words;

    Intent intent;

    SharedPreferences sharedPreferences;
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                char current;
                while (data != -1){
                    current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return "Done";
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        sharedPreferences.edit().putBoolean("add", false).apply();
        sharedPreferences.edit().putBoolean("back", false).apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedPreferences.edit().putBoolean("back", true).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_settings);

        sharedPreferences = this.getSharedPreferences("com.example.tomas.passwordmanager", Context.MODE_PRIVATE);
        intent = new Intent();
        intent.putExtra("password", "");

        capital = (Switch) findViewById(R.id.capitalSwitch);
        number = (Switch) findViewById(R.id.numberSwitch);
        special = (Switch) findViewById(R.id.specialSwitch);

        size = (EditText) findViewById(R.id.lengthEditText);

        words = (CheckBox) findViewById(R.id.wordsCheckBox);

    }

    public void passwordClicked(View view){
        String password = "";
        password = createPassword();

        intent = new Intent(getApplicationContext(), RatePasswordActivity.class);
        intent.putExtra("password", password);
        startActivity(intent);

        Log.i("passwordas", password);
        intent = new Intent();
        intent.putExtra("password", password);
        setResult(1, intent);
        finish();
    }

    public String createPassword(){
        ArrayList<Character> symbols = new ArrayList<Character>();

        String nonCapitalString = "abcdefghijklmnopqrstuvwxyz";
        String capitalString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numberString = "0123456789";
        String specialString = "!?|_-,./()*";

        int passwordSize = 0;

        try{
            if(!size.getText().toString().equals("")) {
                passwordSize = Integer.parseInt(size.getText().toString());
                if(passwordSize >= 100){
                    passwordSize = 100;
                    Toast.makeText(this, "Maximum reached! Size set to 100", Toast.LENGTH_LONG).show();
                }
                Log.i("password", Integer.toString(passwordSize / 5));
            }
            else{
                passwordSize = 8;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Random rand = new Random();
        int specialSize = 0;
        if(special.isChecked()){
            if(passwordSize == 0)specialSize = 1;
            else{
                if(passwordSize>=10)specialSize = rand.nextInt(passwordSize/10);
                specialSize++;
            }
        }
        int capitalSize = 0;
        if(capital.isChecked()){
            if(passwordSize == 0)capitalSize = 1;
            else{
                if(passwordSize>=3)capitalSize = rand.nextInt(passwordSize/3);
                capitalSize++;
            }
        }
        int numberSize = 0;
        if(number.isChecked()){
            if(passwordSize == 0)numberSize = 1;
            else{
                if(passwordSize>=5)numberSize = rand.nextInt(passwordSize/5);
                numberSize++;
            }
        }


        int nonCapitalSize = passwordSize - (specialSize+numberSize+capitalSize);
        if(nonCapitalSize < 0)nonCapitalSize = 0;
        String resultString = "";

        if(words.isChecked()){
            String word = "";
            word += getWord(passwordSize-numberSize-specialSize);

            if(capitalSize>0){
                int k = rand.nextInt(word.length()-specialSize);

                resultString = word.substring(0, k) +word.substring(k, k+specialSize).toUpperCase() + word.substring(k+specialSize);
            }
            else {
                resultString = word;
            }

            nonCapitalSize = 0;
            capitalSize = 0;

        }
        while(specialSize+numberSize+capitalSize+nonCapitalSize>0){
            if(capitalSize>0){
                if(rand.nextBoolean()){
                    resultString += capitalString.charAt(rand.nextInt(capitalString.length()));
                    capitalSize--;
                }
            }
            if(nonCapitalSize>0){
                if(rand.nextBoolean()){
                    resultString += nonCapitalString.charAt(rand.nextInt(nonCapitalString.length()));
                    nonCapitalSize--;
                }
            }
            if(specialSize>0){
                if(rand.nextBoolean()){
                    resultString += specialString.charAt(rand.nextInt(specialString.length()));
                    specialSize--;
                }
            }
            if(numberSize>0){
                if(rand.nextBoolean()){
                    resultString += numberString.charAt(rand.nextInt(numberString.length()));
                    numberSize--;
                }
            }
        }

        return resultString;
    }
    public String getWord(int size){
        String result = null;
        DownloadTask task = new DownloadTask();
        try {
            result = task.execute("https://karklas.mif.vu.lt/~toma3569/").get();
            String[] splitString = result.split("\\n");
            Random rand = new Random();
            int a = rand.nextInt(splitString.length);;

            String resultString = "";
            resultString += splitString[a];
            while(resultString.length() < size) {
                a = rand.nextInt(splitString.length);
                resultString += splitString[a];
            }
            return resultString;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hi";
    }
}
