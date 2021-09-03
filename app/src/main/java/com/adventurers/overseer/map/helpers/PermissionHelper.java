package com.adventurers.overseer.map.helpers;

import static com.adventurers.overseer.Constants.RC_ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Activity;

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
}