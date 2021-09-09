package com.adventurers.overseer.floodforecast.controllers;

import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;

public interface IFloodForecastController {
    ForecastData getForecastDataOnLocation(Location location);
}
