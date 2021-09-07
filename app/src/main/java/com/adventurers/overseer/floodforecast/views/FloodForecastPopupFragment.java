package com.adventurers.overseer.floodforecast.views;

import com.adventurers.overseer.map.models.Location;

public class FloodForecastPopupFragment {
    private final Location location;

    public FloodForecastPopupFragment(double latitude, double longitude) {
        location = new Location(latitude, longitude);
    }

    public Location getLocation() {
        return location;
    }
}
