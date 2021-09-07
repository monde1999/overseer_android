package com.adventurers.overseer.map.controller;

import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.models.MapData;

public interface IMapController {
    MapData getForecastsAroundLocation(Location location, double visibilityRadius);
}
