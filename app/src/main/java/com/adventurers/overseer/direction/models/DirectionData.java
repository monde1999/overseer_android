package com.adventurers.overseer.direction.models;

import android.os.Handler;
import android.os.Looper;

import com.adventurers.overseer.R;
import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DirectionData {
    private List<List<Location>> mRoutes;
    private Location mCurrentLocation;
    private Location mGoal;
    private List<Polyline> mPaths;

    public DirectionData(List<List<Location>> Paths, Location CurrentLocation, Location Goal) {
        mRoutes = Paths;
        mCurrentLocation = CurrentLocation;
        mGoal = Goal;
    }

    public List<List<Location>> getRoutes() {
        return mRoutes;
    }

    public static void renderPath(List<Location> path, GoogleMap map) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                List<LatLng> steps = new ArrayList<>();
                for (Location step : path) {
                    steps.add(new LatLng(step.getLatitude(), step.getLongitude()));
                }
                Polyline polyline = map.addPolyline(new PolylineOptions().addAll(steps));
                polyline.setColor(R.color.main_color);
                polyline.setClickable(true);
            }
        });
    }
}
