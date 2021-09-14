package com.adventurers.overseer.floodforecast.models;

import com.adventurers.overseer.map.models.Location;

public class ForecastData {
    private int hazardLevel;
    private DateTime time;
    private String message;
    private Location location;
    private String messageSummary;

    public int getHazardLevel() {
        return hazardLevel;
    }

    public void setHazardLevel(int hazardLevel) {
        this.hazardLevel = hazardLevel;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getMessageSummary() {
        return messageSummary;
    }

    public void setMessageSummary(String messageSummary) {
        this.messageSummary = messageSummary;
    }
}