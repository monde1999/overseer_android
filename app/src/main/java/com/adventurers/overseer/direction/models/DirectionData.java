package com.adventurers.overseer.direction.models;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.ContextCompat;

import com.adventurers.overseer.R;
import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DirectionData {
    private List<Route> mRoutes;
    private Location mCurrentLocation;
    private Location mGoal;
    private List<Polyline> mPaths;

    public DirectionData(List<Route> Paths, Location CurrentLocation, Location Goal) {
        mRoutes = Paths;
        mCurrentLocation = CurrentLocation;
        mGoal = Goal;
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public static void renderPath(Route route, GoogleMap map, Activity activity) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                List<LatLng> steps = new ArrayList<>();
                for (Location step : route.getRoute()) {
                    steps.add(new LatLng(step.getLatitude(), step.getLongitude()));
                }
                Polyline polyline = map.addPolyline(new PolylineOptions().addAll(steps));
                polyline.setColor(ContextCompat.getColor(activity, R.color.grey));
                polyline.setClickable(true);
            }
        });
    }

    public static void setFirst(Route route, GoogleMap map) {

    }
}