package com.adventurers.overseer.signup.controllers;

public interface ISignupController {
    void addUserToDb(String email, String firstName, String lastName, String password );
}
