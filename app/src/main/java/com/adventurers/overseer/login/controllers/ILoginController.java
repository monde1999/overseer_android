package com.adventurers.overseer.login.controllers;

import com.adventurers.overseer.login.models.LoginData;

public interface ILoginController {
    void getLoginResults(String userName, String password);
}
