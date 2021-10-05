package com.adventurers.overseer.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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
    @POST("report/create/")
    Call<ResponseBody> createReport(@Part("user") int user,
                                    @Part ("description") String description,
                                    @Part("latitude") double latitude,
                                    @Part("longitude") double longitude,
                                    @Part("floodLevel") int floodLevel,
                                    @Part MultipartBody.Part[] images);
}