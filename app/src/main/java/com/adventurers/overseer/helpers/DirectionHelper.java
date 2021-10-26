package com.adventurers.overseer.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.ContextCompat;

import com.adventurers.overseer.R;
import com.adventurers.overseer.direction.models.Route;
import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DirectionHelper {
    private List<Route> mRoutes;
    private Location mCurrentLocation;
    private Location mGoal;
    private List<Polyline> mPolylines;
    private Context mContext;

    public DirectionHelper(List<Route> routes, Location currentLocation, Location goal, Context context) {
        mRoutes = routes;
        mCurrentLocation = currentLocation;
        mGoal = goal;
        mPolylines = new ArrayList<>();
        mContext = context;
    }

    public void renderRoutes(GoogleMap map) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (Route route : mRoutes) {
                    List<LatLng> steps = new ArrayList<>();
                    for (Location step : route.getRoute()) {
                        steps.add(new LatLng(step.getLatitude(), step.getLongitude()));
                    }
                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(steps));
                    polyline.setColor(ContextCompat.getColor(mContext, R.color.grey));
                    polyline.setClickable(true);
                    mPolylines.add(polyline);
                }
            }
        });
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(mPolylines.size()==mRoutes.size()) {
                        // Set first polyline as default route
                        selectPolyline(mPolylines.get(0));
                        break;
                    }
                }
            }
        });
    }

    public void selectPolyline(Polyline polyline) {
        for (Polyline p : mPolylines) {
            if(p.equals(polyline)) {
                p.setColor(ContextCompat.getColor(mContext, R.color.main_color));
                p.setZIndex(1);
            }
            else {
                p.setColor(ContextCompat.getColor(mContext, R.color.grey));
                p.setZIndex(0);
            }
        }
    }

    public List<Polyline> getPolylines() {
        return mPolylines;
    }
}