package com.adventurers.overseer.login.presenters;

import android.os.Handler;
import android.os.Looper;

import com.adventurers.overseer.login.interactors.LoginInteractor;
import com.adventurers.overseer.login.views.ILoginView;

public class LoginPresenter implements ILoginPresenter {
    ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void presentLoginProgressing() {
        loginView.renderLoginProgressing();
    }

    @Override
    public void presentLoginFailure(int errorCode, String errorMessage) {
        loginView.renderLoginFailure(errorCode, errorMessage);
    }

    @Override
    public void presentLoginSuccess() {
        loginView.renderLoginSuccess();
    }

    public void handleLogin(String userName, String password){
        presentLoginProgressing();
        // LoginInteractor is delayed to show LoadingDialog for at least 1 second
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoginInteractor loginInteractor = new LoginInteractor(this);
            loginInteractor.loginUser(userName, password);
        }, 1000);
    }
}
