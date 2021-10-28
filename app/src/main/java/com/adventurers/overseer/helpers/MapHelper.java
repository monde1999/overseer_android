package com.adventurers.overseer.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

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

    public static void moveCameraToBounds(GoogleMap map, LatLngBounds bounds) {
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    public static void adjustMyLocationButton(Activity activity) {
        View locationButton = ((View) activity.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 55, 30, 0);
    }

    public static LatLngBounds getBounds(Polyline polyline) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> arr = polyline.getPoints();
        for(int i = 0; i < arr.size();i++){
            builder.include(arr.get(i));
        }
        return builder.build();
    }
}