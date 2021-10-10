package com.adventurers.overseer.floodforecast.controllers;

import static com.adventurers.overseer.Constants.BASE_URL_WEATHER;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.OPENWEATHER_APPID;

import androidx.annotation.NonNull;

import com.adventurers.overseer.api.openweather.Current;
import com.adventurers.overseer.api.openweather.Daily;
import com.adventurers.overseer.api.openweather.Weather;
import com.adventurers.overseer.api.openweather.WeatherApi;
import com.adventurers.overseer.floodforecast.interactors.ForecastInteractor;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.helpers.TempHelper;
import com.adventurers.overseer.map.models.Location;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FloodForecastController implements IFloodForecastController {
    private final ForecastInteractor mForecastInteractor;
    
    public FloodForecastController(ForecastInteractor forecastInteractor) {
        mForecastInteractor = forecastInteractor;
    }

    @Override
    public ForecastData getForecastDataOnLocation(Location location) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        String exclude = "hourly,minutely,alerts";
        String appid = OPENWEATHER_APPID;
        Call<Weather> call = weatherApi.getWeather(location.getLatitude(), location.getLongitude(), exclude,appid);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    ForecastData forecastData = new ForecastData();
                    Weather weather = response.body();
                    Current current = weather.getCurrent();
                    Daily today = weather.getDaily().get(0);
                    forecastData.setLocation(location);
                    forecastData.setCurrent_temp(TempHelper.toCelsiusInt(current.getTemp()));
                    if(today.getRain()!=null)
                        forecastData.setRain(today.getRain());
                    forecastData.setClouds(current.getClouds());
                    forecastData.setWeather_status(current.getWeather().get(0).getDescription());
                    forecastData.setMorn_temp(TempHelper.toCelsiusInt(today.getTemp().getMorn()));
                    forecastData.setAft_temp(TempHelper.toCelsiusInt(today.getTemp().getDay()));
                    forecastData.setEve_temp(TempHelper.toCelsiusInt(today.getTemp().getEve()));
                    forecastData.setNight_temp(TempHelper.toCelsiusInt(today.getTemp().getNight()));
                    forecastData.setIcon(current.getWeather().get(0).getIcon());
                    mForecastInteractor.onSuccessRequest(forecastData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                onServerRequestFailed(t.getMessage());
            }
        });
        return null;
    }

    private void onServerRequestFailed(String message) {
        mForecastInteractor.showRequestFailure(EC_SERVER_FAILED, message);
    }
}