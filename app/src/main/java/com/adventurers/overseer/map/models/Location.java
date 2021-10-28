package com.adventurers.overseer.map.models;

import androidx.annotation.NonNull;

import com.google.maps.model.LatLng;

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        setLocation(latitude, longitude);
    }

    public Location(LatLng latLng) {
        setLocation(latLng.lat, latLng.lng);
    }

    public Location(com.google.android.gms.maps.model.LatLng latLng) {
        setLocation(latLng.latitude, latLng.longitude);
    }

    public Location(Location location) {
        setLocation(location.getLatitude(), location.getLongitude());
    }

    private void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}
