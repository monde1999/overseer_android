package com.adventurers.overseer.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverseerApi {
    @GET("forecast/flood-prone-areas/")
    Call<List<FloodArea>> getFloodAreas(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
        );

    @GET("forecast/reports/")
    Call<List<ReportData>> getReports(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );

    @GET("forecast/report-images/")
    Call<List<ReportImage>> getImages(
            @Query("report_id") int id
    );

    @GET("forecast/report-reactions-count/")
    Call<ReactionsCount> getReactionsCount(
            @Query("report_id") int id
    );
}