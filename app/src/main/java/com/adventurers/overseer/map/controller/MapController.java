package com.adventurers.overseer.map.controller;


import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.interactors.MapInteractor;
import com.adventurers.overseer.map.models.MapData;

import java.util.ArrayList;
import java.util.List;

public class MapController implements IMapController {
    private final MapInteractor mMapInteractor;

    public MapController(MapInteractor mapInteractor) {
        mMapInteractor = mapInteractor;
    }

    @Override
    public MapData getForecastsAroundLocation(Location location, double visibilityRadius) {
        List<Location> forecasts = new ArrayList<>();
        // APO
        forecasts.add(new Location(10.197100, 123.747842));
        // Caltex
        forecasts.add(new Location(10.239083, 123.779508));
        return new MapData(forecasts);
    }

    private void onServerRequestFailed(){
        mMapInteractor.showRequestFailure(EC_SERVER_FAILED, EM_SERVER_FAILED);
    }
}