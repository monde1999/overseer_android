package com.adventurers.overseer.floodforecast.interactors;

import com.adventurers.overseer.floodforecast.controllers.FloodForecastController;
import com.adventurers.overseer.floodforecast.controllers.IFloodForecastController;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.floodforecast.presenters.IFloodForecastPresenter;
import com.adventurers.overseer.map.models.Location;

import java.util.Timer;

public class ForecastInteractor {
    Timer timer;
    IFloodForecastPresenter mFloodForecastPresenter;

    public ForecastInteractor(IFloodForecastPresenter floodForecastPresenter) {
        mFloodForecastPresenter = floodForecastPresenter;
    }

    public void showForecastOnLocation(Location location) {
        IFloodForecastController controller = new FloodForecastController(this);
        controller.getForecastDataOnLocation(location);
    }

    public void showRequestFailure(int errorCode, String message) {
        mFloodForecastPresenter.presentRequestFailure(errorCode, message);
    }

    public void onSuccessRequest(ForecastData forecastData) {
        mFloodForecastPresenter.presentForecastOnLocation(forecastData);
    }
}