package com.adventurers.overseer.floodforecast.presenters;

import com.adventurers.overseer.floodforecast.models.ForecastData;

public interface IFloodForecastPresenter {
    void presentForecastOnLocation(ForecastData forecastData);
    void presentRequestFailure(int errorCode, String errorMessage);
}