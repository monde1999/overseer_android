package com.adventurers.overseer.map.helpers;

import static com.adventurers.overseer.Constants.RC_GPS_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class MapHelper {
    private final Activity mActivity;
    private final GoogleMap mMap;

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationRequest locationRequest;
    private final LocationCallback locationCallback;

    public MapHelper(Activity activity, GoogleMap map) {
        mActivity = activity;
        mMap = map;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(android.location.Location location : locationResult.getLocations()) {
                    MapHelper.moveCameraToLocation(mMap, location.getLatitude(), location.getLongitude(),
                            15, true);
                }
            }
        };
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    stopFollowDevice();
                }
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                startFollowDevice();
                return false;
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void moveCameraToDeviceLocation() {
        // Gets last known location and zoom to it using successListener
        // Fast but less accurate, used to zoom in faster
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity, successListener);
    }

    private final OnSuccessListener<Location>
            successListener = new OnSuccessListener<android.location.Location>() {
        @Override
        public void onSuccess(android.location.Location location) {
            if(location != null) {
                moveCameraToLocation(mMap, location.getLatitude(), location.getLongitude(), 15, false);
            }
            else {
                // Restart application to obtain device location
                ProcessPhoenix.triggerRebirth(mActivity);
            }
        }
    };

    public void startFollowDevice() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(mActivity);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(mActivity, RC_GPS_SERVICE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    public void stopFollowDevice() {
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

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