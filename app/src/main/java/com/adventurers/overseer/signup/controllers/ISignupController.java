package com.adventurers.overseer.signup.controllers;

import com.adventurers.overseer.signup.models.SignupData;

public interface ISignupController {
    void addUserToDb(String email, String firstName, String lastName, String password );
}
