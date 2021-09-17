package com.adventurers.overseer.direction.controllers;

import static com.adventurers.overseer.Constants.MAPS_API_KEY;

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
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(MAPS_API_KEY).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng destination = new LatLng(goal.getLatitude(), goal.getLongitude());
        // Returns 1 route when DirectionsApiRequest.alternatives is set to false
        directions.alternatives(true);
        directions.origin(origin);
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                List<List<Location>> paths = new ArrayList<>();
                for (DirectionsRoute route : result.routes) {
                    List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<Location> path = new ArrayList<>();
                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (LatLng latLng : decodedPath) {
                        path.add(new Location(latLng.lat, latLng.lng));
                    }
                    paths.add(path);
                }
                mDirectionInteractor.onSuccessRequest(new DirectionData(paths, currentLocation, goal));
            }

            @Override
            public void onFailure(Throwable e) {
                mDirectionInteractor.showRequestFailure(e.hashCode(), e.getMessage());
            }
        });
        // Returns null as onResult and onFailure is not waited
        return null;
    }
}