package com.adventurers.overseer.signup.presenters;

import android.os.Handler;
import android.os.Looper;

import com.adventurers.overseer.signup.interactors.SignupInteractor;
import com.adventurers.overseer.signup.views.ISignUpView;
import com.adventurers.overseer.user.handlers.UserInfoHandler;
import com.adventurers.overseer.user.models.UserInfo;

public class SignupPresenter implements ISignupPresenter {
    ISignUpView signUpView;


    public SignupPresenter(ISignUpView signUpView) {
        this.signUpView = signUpView;
    }

    @Override
    public void presentSignupProgressing() {
        signUpView.renderSignupProgressing();
    }

    @Override
    public void presentSignupFailure(int errorCode, String errorMessage) {
        signUpView.renderSignupFailure(errorCode, errorMessage);
    }

    @Override
    public void presentSignupSuccess() {
        signUpView.renderSignupSuccess();
    }

    @Override
    public void saveCurrentUserToPreferences(UserInfo user, String authToken) {
        UserInfoHandler.setCurrentUser(user,authToken,signUpView.getSharedPreferences());
    }

    public void handleSignup(String email, String firstName, String lastName, String password){
        presentSignupProgressing();
        // SignupInteractor is delayed to show LoadingDialog for at least 1 second
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SignupInteractor signupInteractor = new SignupInteractor(this);
            signupInteractor.signup(email, firstName, lastName, password);
        }, 1000);
    }
}