package com.adventurers.overseer.login.views;

public interface ILoginView {
    void renderLoginFailure(int errorCode, String errorMessage);
    void renderLoginProgressing();
    void renderLoginSuccess();
}
