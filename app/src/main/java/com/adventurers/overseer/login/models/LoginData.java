package com.adventurers.overseer.login.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("username")
    @Expose
    public String username;

    public String password;

    @SerializedName("isUserNameCorrect")
    @Expose
    private boolean isUserNameCorrect;

    @SerializedName("isPasswordCorrect")
    @Expose
    private boolean isPasswordCorrect;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUserNameCorrect() {
        return isUserNameCorrect;
    }

    public boolean isPasswordCorrect() {
        return isPasswordCorrect;
    }

    public void setUserNameCorrect(boolean userNameCorrect) {
        isUserNameCorrect = userNameCorrect;
    }

    public void setPasswordCorrect(boolean passwordCorrect) {
        isPasswordCorrect = passwordCorrect;
    }
}