package com.adventurers.overseer.api;

import com.adventurers.overseer.login.models.LoginData;
import com.adventurers.overseer.signup.models.SignupData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @Multipart
    @POST("report/create/")
    Call<ResponseBody> createReport(@Part("user") int user,
                                    @Part ("description") String description,
                                    @Part("latitude") double latitude,
                                    @Part("longitude") double longitude,
                                    @Part("floodLevel") int floodLevel,
                                    @Part MultipartBody.Part[] images);

    @GET("forecast/reports/")
    Call<List<ReportData>> getReports(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );

    @GET("forecast/report-images/")
    Call<List<ReportImage>> getImages(
            @Query("report_id") int id
    );

    @GET("forecast/report-reactions/")
    Call<List<ReportReaction>> getReportReaction(
            @Query("report_id") int reportId,
            @Query("user_id") int userId
    );

    @GET("forecast/report-reactions-count/")
    Call<ReactionsCount> getReactionsCount(
            @Query("report_id") int id
    );

    @POST("report/react/")
    Call<ReportReactResponse> postReportReaction(@Body ReportReactData reportReactData);
}