package com.adventurers.overseer.map.views;

import static com.adventurers.overseer.Constants.RC_GPS_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.adventurers.overseer.R;
import com.adventurers.overseer.map.helpers.Location;
import com.adventurers.overseer.map.helpers.PermissionHelper;
import com.adventurers.overseer.map.helpers.StatusBarHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback,EasyPermissions.PermissionCallbacks {
    private Location mFocusedLocation;
    private double mVisibilityRadius;
    private boolean mHazardVisibility;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        StatusBarHelper.makeTransparent(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        PermissionHelper.requestLocationAndGPS(this);
        startActivity();
    }

    @SuppressLint("MissingPermission")
    private void startActivity() {
        if(PermissionHelper.isLocationGranted(this) && PermissionHelper.isGPSOn(this) && mMap != null) {
            mMap.setMyLocationEnabled(true);
            findUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void findUserLocation() {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        // Gets last known location and zoom to it using successListener
        // Fast but less accurate, used to zoom in faster
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, successListener);
    }

    private final OnSuccessListener<android.location.Location>
            successListener = new OnSuccessListener<android.location.Location>() {
        @Override
        public void onSuccess(android.location.Location location) {
            if(location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                zoomToLatLng(latLng, 15, false);
            }
        }
    };

    private void zoomToLatLng(LatLng latLng, float zoomLevel, boolean animated) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
        if(animated){
            mMap.animateCamera(cameraUpdate);
        }
        else {
            mMap.moveCamera(cameraUpdate);
        }
    }

    //region : Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        // Some permissions have been granted
        PermissionHelper.requestLocationAndGPS(this);
        startActivity();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        // Some permissions have been denied

        // Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            PermissionHelper.openApplicationInSettings(this);
        }
        else {
            PermissionHelper.requestLocationAndGPS(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen.
            PermissionHelper.requestLocationAndGPS(this);
            startActivity();
        }
        else if(requestCode == RC_GPS_SERVICE) {
            // Do something after GPS is turned on in location dialog
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PermissionHelper.requestLocationAndGPS(this);
                    startActivity();
                    break;
                case Activity.RESULT_CANCELED:
                    PermissionHelper.requestLocationAndGPS(this);
                    break;
            }
        }
    }
    // endregion
}