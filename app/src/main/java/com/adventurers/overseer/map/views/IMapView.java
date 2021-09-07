package com.adventurers.overseer.map.views;

import com.adventurers.overseer.floodforecast.views.FloodForecastPopupFragment;

import java.util.List;

public interface IMapView {
    void renderForecasts(List<FloodForecastPopupFragment> forecasts);
    void renderUserLocation();
    void renderError(int errorCode, String errorString);
}