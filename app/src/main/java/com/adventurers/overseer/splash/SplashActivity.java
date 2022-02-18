package com.adventurers.overseer.splash;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.adventurers.overseer.Constants.preferencesKey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adventurers.overseer.login.views.LoginActivity;
import com.adventurers.overseer.map.views.MapActivity;
import com.adventurers.overseer.user.handlers.UserInfoHandler;
import com.adventurers.overseer.user.models.UserInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        //use this to delete all stored info
        //for testing
        //sharedPreferences.edit().clear().commit();
        loadOverseerServerIp();
        if(UserInfoHandler.hasAccountStored(sharedPreferences)){
            UserInfo currentUser = UserInfoHandler.getCurrentUser(sharedPreferences);
            String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            //show simple welcome message
            Toast.makeText(getApplicationContext(), "Welcome back, "+fullName,Toast.LENGTH_LONG).show();
            startActivity(new Intent(SplashActivity.this, MapActivity.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    public void loadOverseerServerIp() {
        File file = new File(getFilesDir(), "config.txt");
        if(file.exists()) {
            try {
                InputStream inputStream = openFileInput("config.txt");
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    BASE_URL_OVERSEER = stringBuilder.toString();
                    Toast.makeText(this, BASE_URL_OVERSEER, Toast.LENGTH_SHORT).show();
                }
            }
            catch (FileNotFoundException e) {
                Toast.makeText(this, "No configuration found.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error loading configuration.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}