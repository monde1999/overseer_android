package com.adventurers.overseer.signup.interactors;

import com.adventurers.overseer.signup.controllers.ISignupController;
import com.adventurers.overseer.signup.controllers.SignupController;
import com.adventurers.overseer.signup.presenters.ISignupPresenter;
import com.adventurers.overseer.user.models.UserInfo;

public class SignupInteractor {
    ISignupPresenter signupPresenter;


    public SignupInteractor(ISignupPresenter signupPresenter) {
        this.signupPresenter = signupPresenter;
    }

    public void signup(String email, String firstName, String lastName, String password){
        ISignupController signupController = new SignupController(this);;
        signupController.addUserToDb(email, firstName, lastName, password);
    }

    public void feedBackSignupFailure(int errorCode, String errorMessage){
        signupPresenter.presentSignupFailure(errorCode, errorMessage);
    }

    public void feedBackSignupSuccess(){
        signupPresenter.presentSignupSuccess();
    }
    public void rememberUser(UserInfo user, String authToken){
        signupPresenter.saveCurrentUserToPreferences(user,authToken);
    }
}
