package com.adventurers.overseer.signup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupData {
    @SerializedName("isEmailUnique")
    @Expose
    private boolean isEmailUnique;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id")
    @Expose
    private int userId;


    public boolean getIsEmailUnique() {
        return isEmailUnique;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

}