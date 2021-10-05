package com.adventurers.overseer.signup.views;

import android.content.SharedPreferences;

public interface ISignUpView {
    void renderSignupFailure(int errorCode, String errorMessage);
    void renderSignupProgressing();
    void renderSignupSuccess();
    SharedPreferences getSharedPreferences();
}
