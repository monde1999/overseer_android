package com.adventurers.overseer.splash;

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

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        //use this to delete all stored info
        //for testing
        //sharedPreferences.edit().clear().commit();
        if(UserInfoHandler.hasAccountStored(sharedPreferences)){
            UserInfo currentUser = UserInfoHandler.getCurrentUser(sharedPreferences);
            String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            //show simple welcome message
            Toast.makeText(getApplicationContext(), "Welcome Back, "+fullName,Toast.LENGTH_LONG).show();
            startActivity(new Intent(SplashActivity.this, MapActivity.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}