package com.adventurers.overseer.login.presenters;

import com.adventurers.overseer.user.models.UserInfo;

public interface ILoginPresenter {
    void presentLoginProgressing();
    void presentLoginFailure(int errorCode, String errorMessage);
    void presentLoginSuccess();
    void saveCurrentUserToPreferences(UserInfo user, String authToken);
}
