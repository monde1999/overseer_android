package com.adventurers.overseer.signup.api;

import com.adventurers.overseer.signup.models.SignupData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignUpApi {
    @Multipart
    @POST("account/signup/")
    Call<SignupData> SignUp(@Part("username") RequestBody username,
                            @Part("password") RequestBody password,
                            @Part("firstName") RequestBody firstName,
                            @Part("lastName") RequestBody lastName);
}
