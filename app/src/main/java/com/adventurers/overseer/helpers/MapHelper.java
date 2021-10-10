package com.adventurers.overseer.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

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

    public static void adjustMyLocationButton(Activity activity) {
        View locationButton = ((View) activity.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 55, 30, 0);
    }
}