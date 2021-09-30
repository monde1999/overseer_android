package com.adventurers.overseer.login.controllers;

import static com.adventurers.overseer.Constants.EC_ACCOUNT_NOT_EXIST;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EC_WRONG_PASSWORD;
import static com.adventurers.overseer.Constants.EM_ACCOUNT_NOT_EXIST;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_WRONG_PASSWORD;

import com.adventurers.overseer.login.interactors.LoginInteractor;
import com.adventurers.overseer.login.models.LoginData;

public class LoginController implements ILoginController {
    private final LoginInteractor loginInteractor;

    public LoginController(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void getLoginResults(LoginData loginData) {
        // Mocking server behavior
        // Note: In LoginData the default value for isUsernameCorrect and isPasswordCorrect is false
        if(loginData.getUsername().equals("overseer@gmail.com")) {
            loginData.setUserNameCorrect(true);
        }
        if(loginData.getPassword().equals("Overseer123")) {
            loginData.setPasswordCorrect(true);
        }
        // In case the server failed example
        boolean serverFailed = false;
        if(serverFailed) {
            onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
            return;
        }

        if(!loginData.isUserNameCorrect()) {
            loginInteractor.feedBackLoginFailure(EC_ACCOUNT_NOT_EXIST, EM_ACCOUNT_NOT_EXIST);
        }
        else if(!loginData.isPasswordCorrect()) {
            loginInteractor.feedBackLoginFailure(EC_WRONG_PASSWORD, EM_WRONG_PASSWORD);
        }
        else{
            onServerRequestSuccess();
        }
    }

    private void onServerRequestFailed(int code, String errorMessage) {
        loginInteractor.feedBackLoginFailure(code, errorMessage);
    }

    private void onServerRequestSuccess() {
        loginInteractor.feedBackLoginSuccess();
    }
}