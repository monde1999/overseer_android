package com.adventurers.overseer.floodforecast.views;

import android.content.Context;

import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class FloodForecast {
    private final List<FloodForecastPopupFragment> popupFragments;
    private GoogleMap mMap;

    public FloodForecast(List<Location> locations, Context context, GoogleMap map) {
        popupFragments = new ArrayList<>();
        for (Location location : locations) {
            popupFragments.add(new FloodForecastPopupFragment(location, context, map));
        }
    }

    public List<FloodForecastPopupFragment> getPopupFragments() {
        return popupFragments;
    }
}