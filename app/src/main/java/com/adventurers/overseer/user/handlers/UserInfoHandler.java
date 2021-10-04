package com.adventurers.overseer.user.handlers;

import android.content.SharedPreferences;
import com.adventurers.overseer.user.models.UserInfo;

public class UserInfoHandler {

    public static void setCurrentUser(UserInfo user,String token,SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", user.getUsername());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName",user.getLastName());
        editor.putInt("userId", user.getId());
        editor.putString("token",token);
        editor.apply();
    }
    public static String getCurrentAccountToken(SharedPreferences preferences){
        String token = null;
        if(hasAccountStored(preferences)){
            token = preferences.getString("token","no token");
        }
        return token;
    }
    public void setCurrentAccountToken(String token, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
    }
    public static boolean hasAccountStored(SharedPreferences preferences){
        return !(preferences.getString("username","not found")).equals("not found");
    }
    public static UserInfo getCurrentUser(SharedPreferences preferences){
        UserInfo currentUser = null;
        if (hasAccountStored(preferences)) {
            String username = preferences.getString("username", "not found");
            String firstName = preferences.getString("firstName", "not found");
            String lastName = preferences.getString("lastName", "not found");
            int id = preferences.getInt("userId",-1);
            currentUser = new UserInfo(username, null, firstName, lastName, id);
        }
        return currentUser;
    }
}
