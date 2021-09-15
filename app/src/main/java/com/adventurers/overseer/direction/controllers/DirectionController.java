package com.adventurers.overseer.direction.controllers;

import static com.adventurers.overseer.Constants.MAPS_API_KEY;
import static com.adventurers.overseer.Constants.TAG_DIRECTION_MODULE;

import android.util.Log;

import com.adventurers.overseer.direction.interactors.DirectionInteractor;
import com.adventurers.overseer.direction.models.DirectionData;
import com.adventurers.overseer.map.models.Location;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DirectionController implements IDirectionController {
    private final DirectionInteractor mDirectionInteractor;

    public DirectionController(DirectionInteractor directionInteractor) {
        mDirectionInteractor = directionInteractor;
    }

    @Override
    public DirectionData findPath(Location currentLocation, Location goal) {
        Log.d(TAG_DIRECTION_MODULE, MAPS_API_KEY);
        List<Location> path = new ArrayList<>();
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(MAPS_API_KEY).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng destination = new LatLng(goal.getLatitude(), goal.getLongitude());
        // Returns 1 route when DirectionsApiRequest.alternatives is set to false
        directions.alternatives(false);
        directions.origin(origin);
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                for(DirectionsRoute route : result.routes) {
                    List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(LatLng latLng: decodedPath){
                        path.add(new Location(latLng.lat, latLng.lng));
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {
                mDirectionInteractor.showRequestFailure(e.hashCode(), e.getMessage());
            }
        });
        if(!path.isEmpty()) return new DirectionData(path, currentLocation, goal);
        else return null;
    }
}