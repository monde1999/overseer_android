package com.adventurers.overseer.server;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface
ReportApi {
    @Multipart
    @POST("create/")
    Call<ResponseBody> createReport(@Part("user") int user,
                                    @Part ("description") String description,
                                    @Part("latitude") double latitude,
                                    @Part("longitude") double longitude,
                                    @Part("floodLevel") int floodLevel,
                                    @Part MultipartBody.Part[] images);
}