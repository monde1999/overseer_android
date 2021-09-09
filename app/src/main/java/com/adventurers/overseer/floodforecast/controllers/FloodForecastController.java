package com.adventurers.overseer.floodforecast.controllers;

import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;

public class FloodForecastController implements IFloodForecastController {

    @Override
    public ForecastData getForecastDataOnLocation(Location location) {
        return null;
    }

    private void onServerRequestFailed(){
    }
}