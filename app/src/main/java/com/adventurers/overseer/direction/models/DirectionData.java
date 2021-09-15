package com.adventurers.overseer.direction.models;

import com.adventurers.overseer.map.models.Location;

import java.util.List;

public class DirectionData {
    private List<Location> path;
    private Location currentLocation;
    private Location goal;

    public List<Location> getPath() {
        return path;
    }

    public void setPath(List<Location> path) {
        this.path = path;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getGoal() {
        return goal;
    }

    public void setGoal(Location goal) {
        this.goal = goal;
    }
}
