package com.adventurers.overseer.signup.presenters;

import com.adventurers.overseer.user.models.UserInfo;

public interface ISignupPresenter {
    void presentSignupProgressing();
    void presentSignupFailure(int errorCode, String errorMessage);
    void presentSignupSuccess();
    void saveCurrentUserToPreferences(UserInfo user, String authToken);
}
