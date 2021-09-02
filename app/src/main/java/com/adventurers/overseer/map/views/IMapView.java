package com.adventurers.overseer.map.views;

public interface IMapView {
//    void renderForecasts(List<FloodForecastPopupFragment> forecasts);
    void renderUserLocation();
    void renderError(int errorCode, String errorString);
}