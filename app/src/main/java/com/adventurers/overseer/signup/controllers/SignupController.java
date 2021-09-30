package com.adventurers.overseer.signup.controllers;

import static com.adventurers.overseer.Constants.EC_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import com.adventurers.overseer.signup.interactors.SignupInteractor;
import com.adventurers.overseer.signup.models.SignupData;

public class SignupController implements ISignupController {
    SignupInteractor signupInteractor;

    public SignupController(SignupInteractor signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    @Override
    public void addUserToDb(SignupData signupData) {
        // Mocking server behavior
        boolean serverFailed = false;
        if(serverFailed) {
            onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
            return;
        }
        if(signupData.getUsername().equals("overseer@gmail.com")) {
            onServerRequestFailed(EC_EMAIL_REGISTERED, EM_EMAIL_REGISTERED);
        }
        else {
            onServerRequestSuccess();
        }
    }

    public void onServerRequestSuccess(){
        signupInteractor.feedBackLoginSuccess();
    }

    public void onServerRequestFailed(int code, String errorMessage){
        signupInteractor.feedBackSignupFailure(code, errorMessage);
    }
}