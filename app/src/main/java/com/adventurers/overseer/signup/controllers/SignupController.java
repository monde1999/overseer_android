package com.adventurers.overseer.signup.controllers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.adventurers.overseer.Constants.EC_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EC_SERVER_ERROR;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_EMAIL_REGISTERED;
import static com.adventurers.overseer.Constants.EM_SERVER_ERROR;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import androidx.annotation.NonNull;

import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.signup.interactors.SignupInteractor;
import com.adventurers.overseer.signup.models.SignupData;
import com.adventurers.overseer.user.models.UserInfo;

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
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"),email);
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"),password);
        RequestBody fName = RequestBody.create(MediaType.parse("text/plain"),firstName);
        RequestBody lName = RequestBody.create(MediaType.parse("text/plain"),lastName);
        Call<SignupData> call = overseerApi.SignUp(user, pass,
                fName, lName);
        call.enqueue(new Callback<SignupData>() {
            @Override
            public void onResponse(@NonNull Call<SignupData> call, @NonNull Response<SignupData> response) {
                if(!response.isSuccessful()) {
                    onServerRequestFailed(EC_SERVER_ERROR,EM_SERVER_ERROR);
                    return;
                }
                if (response.body() != null) {
                    SignupData result = response.body();
                    if(!result.getIsEmailUnique()){
                        onServerRequestFailed(EC_EMAIL_REGISTERED, EM_EMAIL_REGISTERED);
                    }
                    else{
                        signupInteractor.rememberUser(new UserInfo(email,null,firstName,lastName,result.getUserId()),result.getToken());
                        onServerRequestSuccess();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<SignupData> call, @NonNull Throwable t) {
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