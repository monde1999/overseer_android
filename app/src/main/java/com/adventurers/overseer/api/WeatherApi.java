package com.adventurers.overseer.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/onecall")
    Call<Weather> getWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("exclude") String exclude,
            @Query("appid") String appid
    );
}
