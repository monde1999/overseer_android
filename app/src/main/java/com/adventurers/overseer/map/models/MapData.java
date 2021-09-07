package com.adventurers.overseer.map.models;

import java.util.List;

public class MapData {
    private List<Location> mForecasts;

    public MapData(List<Location> forecasts){
        mForecasts = forecasts;
    }

    public List<Location> getForecasts() {
        return mForecasts;
    }
}