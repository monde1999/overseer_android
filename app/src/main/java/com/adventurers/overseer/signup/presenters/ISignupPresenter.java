package com.adventurers.overseer.signup.presenters;

public interface ISignupPresenter {
    void presentSignupProgressing();
    void presentSignupFailure(int errorCode, String errorMessage);
    void presentSignupSuccess();
}
