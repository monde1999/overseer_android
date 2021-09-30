package com.adventurers.overseer.login.presenters;

public interface ILoginPresenter {
    void presentLoginProgressing();
    void presentLoginFailure(int errorCode, String errorMessage);
    void presentLoginSuccess();
}
