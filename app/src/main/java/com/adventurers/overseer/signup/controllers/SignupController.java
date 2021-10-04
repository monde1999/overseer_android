package com.adventurers.overseer.signup.controllers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.adventurers.overseer.Constants.EC_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import com.adventurers.overseer.signup.api.SignUpApi;
import com.adventurers.overseer.signup.interactors.SignupInteractor;
import com.adventurers.overseer.signup.models.SignupData;
import com.adventurers.overseer.user.handlers.UserInfoHandler;
import com.adventurers.overseer.user.models.UserInfo;

import java.util.concurrent.RecursiveTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupController implements ISignupController {
    SignupInteractor signupInteractor;
    Retrofit retrofit;

    public SignupController(SignupInteractor signupInteractor) {
        this.signupInteractor = signupInteractor;
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void addUserToDb(String email, String firstName, String lastName, String password) {
        SignUpApi signUpApi = retrofit.create(SignUpApi.class);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"),password);
        RequestBody fName = RequestBody.create(MediaType.parse("text/plain"),firstName);
        RequestBody lName = RequestBody.create(MediaType.parse("text/plain"),lastName);
        Call<SignupData> call = signUpApi.SignUp(user, pass,
                fName, lName);
        call.enqueue(new Callback<SignupData>() {
            @Override
            public void onResponse(Call<SignupData> call, Response<SignupData> response) {
                if (response.isSuccessful()){
                    SignupData result = response.body();
                    if(!result.getIsEmailUnique()){
                        onServerRequestFailed(EC_EMAIL_REGISTERED, EM_EMAIL_REGISTERED);
                    }
                    else{
                        signupInteractor.rememberUser(new UserInfo(email,null,firstName,lastName,result.getUserId()),result.getToken());
                        onServerRequestSuccess();
                    }
                }
                else {
                    onServerRequestFailed(EC_SERVER_FAILED,EM_SERVER_FAILED);
                }
            }
            @Override
            public void onFailure(Call<SignupData> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Sign Up. Unable to Connect");
                onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
            }
        });
    }

    public void onServerRequestSuccess(){
        signupInteractor.feedBackSignupSuccess();
    }

    public void onServerRequestFailed(int code, String errorMessage){
        signupInteractor.feedBackSignupFailure(code, errorMessage);
    }
}