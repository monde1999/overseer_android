package com.adventurers.overseer.direction.models;

import com.adventurers.overseer.map.models.Location;

import java.util.List;

public class DirectionData {
    private List<Route> mRoutes;
    private Location mCurrentLocation;
    private Location mGoal;

    public DirectionData(List<Route> routes, Location currentLocation, Location goal) {
        mRoutes = routes;
        mCurrentLocation = currentLocation;
        mGoal = goal;
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }
}