package com.adventurers.overseer.floodforecast.controllers;

import static com.adventurers.overseer.Constants.BASE_URL_WEATHER;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import com.adventurers.overseer.api.Current;
import com.adventurers.overseer.api.Daily;
import com.adventurers.overseer.api.Weather;
import com.adventurers.overseer.api.WeatherApi;
import com.adventurers.overseer.helpers.TempHelper;
import com.adventurers.overseer.floodforecast.interactors.ForecastInteractor;
import com.adventurers.overseer.floodforecast.models.ForecastData;
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
    
//    @Override
//    public ForecastData getForecastDataOnLocation(Location location) {
//        ForecastData forecastData = new ForecastData();
//        if(location.getLatitude() == 10.197100) {
//            forecastData.setMessage("City of Naga");
//        }
//        else if(location.getLatitude() == 10.239083) {
//            forecastData.setMessage("Minglanilla");
//        }
//        else {
//            forecastData.setMessage("Unknown location.");
//        }
//        return forecastData;
//    }

    @Override
    public ForecastData getForecastDataOnLocation(Location location) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        String exclude = "hourly,minutely,alerts";
        String appid = "67aa636d02df1df62ef01de2db58fa49";
        Call<Weather> call = weatherApi.getWeather(location.getLatitude(), location.getLongitude(), exclude,appid);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(!response.isSuccessful()){
                    return;
                }
                ForecastData forecastData = new ForecastData();
                forecastData.setMessage("default");
                if (response.body() != null) {
                    Weather weather = response.body();
                    Current current = weather.getCurrent();
                    Daily today = weather.getDaily().get(0);
                    forecastData.setLocation(location);
                    forecastData.setCurrent_temp((int)TempHelper.toCelsiusInt(current.getTemp()));
                    forecastData.setRain(today.getRain());
                    forecastData.setClouds(current.getClouds());
                    forecastData.setWeather_status(current.getWeather().get(0).getDescription());
                    forecastData.setMorn_temp(TempHelper.toCelsiusInt(today.getTemp().getMorn()));
                    forecastData.setAft_temp(TempHelper.toCelsiusInt(today.getTemp().getDay()));
                    forecastData.setEve_temp(TempHelper.toCelsiusInt(today.getTemp().getEve()));
                    forecastData.setNight_temp(TempHelper.toCelsiusInt(today.getTemp().getNight()));
                    forecastData.setIcon(current.getWeather().get(0).getIcon());
                }
                mForecastInteractor.onSuccessRequest(forecastData);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                ForecastData forecastData = new ForecastData();
                forecastData.setMessage("failed"+t.getCause());
                mForecastInteractor.onSuccessRequest(forecastData);
                onServerRequestFailed();
            }
        });
        return null;
    }

    private void onServerRequestFailed() {
        mForecastInteractor.showRequestFailure(EC_SERVER_FAILED, EM_SERVER_FAILED);
    }
}