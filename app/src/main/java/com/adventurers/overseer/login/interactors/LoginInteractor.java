package com.adventurers.overseer.login.interactors;

import com.adventurers.overseer.login.controllers.ILoginController;
import com.adventurers.overseer.login.controllers.LoginController;
import com.adventurers.overseer.login.models.LoginData;
import com.adventurers.overseer.login.presenters.ILoginPresenter;
import com.adventurers.overseer.user.models.UserInfo;

public class LoginInteractor {
    private final ILoginPresenter loginPresenter;

    public LoginInteractor(ILoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;

    }

    public void loginUser(String userName, String password){
        ILoginController loginController = new LoginController(this);
        loginController.getLoginResults(userName,password);
    }

    public void feedBackLoginFailure(int errorCode, String errorMessage){
        loginPresenter.presentLoginFailure(errorCode, errorMessage);
    }

    public void feedBackLoginSuccess(){
        loginPresenter.presentLoginSuccess();
    }
    public void rememberUserLogin(UserInfo user, String authToken){
        loginPresenter.saveCurrentUserToPreferences(user, authToken);
    }
}