package com.adventurers.overseer.direction.models;

import com.adventurers.overseer.map.models.Location;

import java.util.List;

public class DirectionData {
    private List<Route> routes;
    private Location currentLocation;
    private Location goal;

    public DirectionData(List<Route> routes, Location currentLocation, Location goal) {
        this.routes = routes;
        this.currentLocation = currentLocation;
        this.goal = goal;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}