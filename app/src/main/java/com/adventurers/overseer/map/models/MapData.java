package com.adventurers.overseer.map.models;

import java.util.List;

public class MapData {
    private final List<Location> mForecasts;
    private final List<Integer> mIds;

    public MapData(List<Location> forecasts, List<Integer> ids){
        mForecasts = forecasts;
        mIds = ids;
    }

    public List<Location> getForecasts() {
        return mForecasts;
    }
    public List<Integer> getIds() { return mIds; }
}