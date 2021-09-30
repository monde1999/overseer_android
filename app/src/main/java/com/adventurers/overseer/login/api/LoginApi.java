package com.adventurers.overseer.login.api;

import com.adventurers.overseer.login.models.LoginData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginApi {
    @Multipart
    @POST("login/")
    Call<LoginData> Login(@Part("username") RequestBody username,
                          @Part("password") RequestBody password);
}
