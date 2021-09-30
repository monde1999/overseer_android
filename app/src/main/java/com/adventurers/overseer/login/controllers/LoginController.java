package com.adventurers.overseer.login.controllers;

import static com.adventurers.overseer.Constants.EC_ACCOUNT_NOT_EXIST;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EC_WRONG_PASSWORD;
import static com.adventurers.overseer.Constants.EM_ACCOUNT_NOT_EXIST;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_WRONG_PASSWORD;

import com.adventurers.overseer.login.api.LoginApi;
import com.adventurers.overseer.login.interactors.LoginInteractor;
import com.adventurers.overseer.login.models.LoginData;
import com.adventurers.overseer.user.handlers.UserInfoHandler;
import com.adventurers.overseer.user.models.UserInfo;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController implements ILoginController {

    private final LoginInteractor loginInteractor;
    private static final String BASE_URL = "http://192.168.0.13:8000/account/"; // change to host ip of server accordingly
    Retrofit retrofit;
    public LoginController(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getLoginResults(String username, String password) {
        LoginApi loginApi = retrofit.create(LoginApi.class);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),username);
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"),password);
        Call<LoginData> call = loginApi.Login(user,pass);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()){
                    LoginData result = response.body();
                    if (!result.isUserNameCorrect()){
                        loginInteractor.feedBackLoginFailure(EC_ACCOUNT_NOT_EXIST, EM_ACCOUNT_NOT_EXIST);
                    }
                    else if (!result.isPasswordCorrect()){
                        loginInteractor.feedBackLoginFailure(EC_WRONG_PASSWORD, EM_WRONG_PASSWORD);
                    }
                    else{
                        UserInfo currentUser = new UserInfo(response.body().getUsername(),null,
                                response.body().getFirstName(),response.body().getLastName(),response.body().getId());
                        loginInteractor.rememberUserLogin(currentUser,response.body().getToken());
                        loginInteractor.feedBackLoginSuccess();
                        System.out.println("Login Success");
                        onServerRequestSuccess();
                    }
                }
                else {
                    onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
                }
            }
            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Login. Unable to Connect");
                onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
            }
        });
    }

    private void onServerRequestFailed(int code, String errorMessage) {
        loginInteractor.feedBackLoginFailure(code, errorMessage);
    }

    private void onServerRequestSuccess() {
        loginInteractor.feedBackLoginSuccess();
    }
}