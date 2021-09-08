package com.adventurers.overseer.floodforecast.views;

import com.adventurers.overseer.floodforecast.models.ForecastData;

public interface IFloodForecastView {
    void renderForecastOnLocation(ForecastData forecastData);
}