package com.adventurers.overseer.signup.views;

public interface ISignUpView {
    void renderSignupFailure(int errorCode, String errorMessage);
    void renderSignupProgressing();
    void renderSignupSuccess();
}
