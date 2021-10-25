package com.adventurers.overseer.direction.models;

import com.adventurers.overseer.map.models.Location;

import java.util.List;

public class Route {
    private final List<Location> route;

    public Route(List<Location> route) {
        this.route = route;
    }

    public List<Location> getRoute() {
        return route;
    }
}