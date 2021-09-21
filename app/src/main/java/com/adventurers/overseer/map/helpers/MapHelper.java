package com.adventurers.overseer.map.helpers;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapHelper {
    public static void moveCameraToLocation(GoogleMap map, double latitude, double longitude,
                                            float zoomLevel, boolean animated) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
        if(animated){
            map.animateCamera(cameraUpdate);
        }
        else {
            map.moveCamera(cameraUpdate);
        }
    }
}