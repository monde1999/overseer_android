package com.adventurers.overseer.login.views;

import android.content.SharedPreferences;

public interface ILoginView {
    void renderLoginFailure(int errorCode, String errorMessage);
    void renderLoginProgressing();
    void renderLoginSuccess();
    SharedPreferences getSharedPreferences();
}
