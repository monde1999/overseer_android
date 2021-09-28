package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.RC_ACCESS_FINE_LOCATION;
import static com.adventurers.overseer.Constants.RC_GPS_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import pub.devrel.easypermissions.AppSettingsDialog;

public class PermissionHelper {
    public static void requestLocation(Activity activity){
        String[] PERMISSION = { Manifest.permission.ACCESS_FINE_LOCATION };
        activity.requestPermissions(PERMISSION, RC_ACCESS_FINE_LOCATION);
    }

    public static void openApplicationInSettings(Activity activity) {
        new AppSettingsDialog.Builder(activity)
                .setRationale("Location Permission is required by the app to work properly. " +
                        "You can enable it in settings.")
                .setPositiveButton("Settings")
                .setNegativeButton("Cancel")
                .build().show();
    }

    public static void requestGPS(Activity activity){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    // No error means Location is On
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    // Error means Location is Off
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(activity, RC_GPS_SERVICE);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                break;
                            }
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }

            }
        });
    }

    public static boolean isGPSOn(Activity activity){
        LocationManager locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = false;
        try {
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}
        return gps;
    }

    public static boolean isLocationGranted(Activity activity){
        boolean location = true;
        if (ActivityCompat
                .checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            location = false;
        }
        return location;
    }

    public static void ensureLocationAndGPS(Activity activity) {
        // Check if Location Permission is granted
        if (!PermissionHelper.isLocationGranted(activity)) {
            PermissionHelper.requestLocation(activity);
        }
        else{
            // Check if Location Service is on
            if(!PermissionHelper.isGPSOn(activity)) {
                PermissionHelper.requestGPS(activity);
            }
        }
    }
}