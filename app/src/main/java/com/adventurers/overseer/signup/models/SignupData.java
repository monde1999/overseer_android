package com.adventurers.overseer.signup.models;

public class SignupData {
    private String username;
    private String firstName;
    private String lastName;
    private String password;

    public SignupData(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}