package com.adventurers.overseer.floodforecast.controllers;

import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import com.adventurers.overseer.floodforecast.interactors.ForecastInteractor;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;

public class FloodForecastController implements IFloodForecastController {
    private final ForecastInteractor mForecastInteractor;
    
    public FloodForecastController(ForecastInteractor forecastInteractor) {
        mForecastInteractor = forecastInteractor;
    }
    
    @Override
    public ForecastData getForecastDataOnLocation(Location location) {
        ForecastData forecastData = new ForecastData();
        if(location.getLatitude() == 10.197100) {
            forecastData.setMessage("City of Naga");
        }
        else if(location.getLatitude() == 10.239083) {
            forecastData.setMessage("Minglanilla");
        }
        else {
            forecastData.setMessage("Unknown location.");
        }
        return forecastData;
    }

    private void onServerRequestFailed() {
        mForecastInteractor.showRequestFailure(EC_SERVER_FAILED, EM_SERVER_FAILED);
    }
}