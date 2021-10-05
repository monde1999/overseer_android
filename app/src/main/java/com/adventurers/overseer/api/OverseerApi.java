package com.adventurers.overseer.api;

import com.adventurers.overseer.login.models.LoginData;
import com.adventurers.overseer.signup.models.SignupData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface OverseerApi {
    @GET("forecast/flood-prone-areas/")
    Call<List<FloodArea>> getFloodAreas(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
        );

    @Multipart
    @POST("account/signup/")
    Call<SignupData> SignUp(@Part("username") RequestBody username,
                            @Part("password") RequestBody password,
                            @Part("firstName") RequestBody firstName,
                            @Part("lastName") RequestBody lastName);

    @Multipart
    @POST("account/login/")
    Call<LoginData> Login(@Part("username") RequestBody username,
                          @Part("password") RequestBody password);
}