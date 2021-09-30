package com.adventurers.overseer.login.models;

public class LoginData {
    private final String username;
    private final String password;
    private boolean isUserNameCorrect;
    private boolean isPasswordCorrect;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
        isUserNameCorrect = false;
        isPasswordCorrect = false;
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