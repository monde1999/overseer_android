package com.adventurers.overseer.floodforecast.models;

import com.adventurers.overseer.map.models.Location;

public class ForecastData {
    private int hazardLevel;
    private DateTime time;
    private String message;
    private Location location;
    private String messageSummary;

    private int current_temp;
    private int clouds;
    private double rain;
    private String weather_status;
    private int morn_temp;
    private int aft_temp;
    private int eve_temp;
    private int night_temp;
    private String icon;

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

    public int getCurrent_temp() {
        return current_temp;
    }

    public void setCurrent_temp(int current_temp) {
        this.current_temp = current_temp;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public String getWeather_status() {
        return weather_status;
    }

    public void setWeather_status(String weather_status) {
        this.weather_status = weather_status;
    }

    public int getMorn_temp() {
        return morn_temp;
    }

    public void setMorn_temp(int morn_temp) {
        this.morn_temp = morn_temp;
    }

    public int getAft_temp() {
        return aft_temp;
    }

    public void setAft_temp(int aft_temp) {
        this.aft_temp = aft_temp;
    }

    public int getEve_temp() {
        return eve_temp;
    }

    public void setEve_temp(int eve_temp) {
        this.eve_temp = eve_temp;
    }

    public int getNight_temp() {
        return night_temp;
    }

    public void setNight_temp(int night_temp) {
        this.night_temp = night_temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}