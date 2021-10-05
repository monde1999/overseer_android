package com.adventurers.overseer.login.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("isUserNameCorrect")
    @Expose
    private boolean isUserNameCorrect;

    @SerializedName("isPasswordCorrect")
    @Expose
    private boolean isPasswordCorrect;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUserNameCorrect() {
        return isUserNameCorrect;
    }

    public boolean isPasswordCorrect() {
        return isPasswordCorrect;
    }
}