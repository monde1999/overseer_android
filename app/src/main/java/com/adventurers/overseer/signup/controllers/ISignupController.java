package com.adventurers.overseer.signup.controllers;

import com.adventurers.overseer.signup.models.SignupData;

public interface ISignupController {
    void addUserToDb(SignupData signupData);
}
