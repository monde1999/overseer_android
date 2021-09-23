package com.adventurers.overseer.floodforecast.presenters;

import com.adventurers.overseer.floodforecast.interactors.ForecastInteractor;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.floodforecast.views.FloodForecastDetailedFragment;
import com.adventurers.overseer.floodforecast.views.IFloodForecastView;
import com.adventurers.overseer.map.models.Location;

public class FloodForecastPresenter implements IFloodForecastPresenter {
    IFloodForecastView mFloodForecastView;

    public FloodForecastPresenter(IFloodForecastView floodForecastView) {
        mFloodForecastView = floodForecastView;
    }

    @Override
    public void presentForecastOnLocation(ForecastData forecastData) {
        mFloodForecastView.renderForecastOnLocation(forecastData);
    }

    @Override
    public void presentRequestFailure(int errorCode, String errorMessage) {

    }

    public void present(Location location) {
        ForecastInteractor forecastInteractor = new ForecastInteractor(this);
        forecastInteractor.showForecastOnLocation(location);
    }
}