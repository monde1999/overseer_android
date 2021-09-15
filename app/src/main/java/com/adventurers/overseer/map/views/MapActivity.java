package com.adventurers.overseer.map.views;

import static com.adventurers.overseer.Constants.RC_GPS_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.adventurers.overseer.R;
import com.adventurers.overseer.direction.presenters.DirectionPresenter;
import com.adventurers.overseer.direction.views.IDirectionView;
import com.adventurers.overseer.floodforecast.views.FloodForecast;
import com.adventurers.overseer.floodforecast.views.FloodForecastPopupFragment;
import com.adventurers.overseer.map.helpers.MapHelper;
import com.adventurers.overseer.map.helpers.PermissionHelper;
import com.adventurers.overseer.map.helpers.StatusBarHelper;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.presenters.MapPresenter;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback,EasyPermissions.PermissionCallbacks, IMapView, IDirectionView {
    private Location mFocusedLocation;
    private double mVisibilityRadius;
    private boolean mHazardVisibility;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private List<FloodForecastPopupFragment> mPopupFragments;

    private static final String TAG = "MapActivity";

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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Called when device location is updated
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(android.location.Location location : locationResult.getLocations()) {
                    MapHelper.moveCameraToLocation(mMap, location.getLatitude(), location.getLongitude(),
                            15, true);
                }
                onActorMove();
            }
        };
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
        if(PermissionHelper.isLocationGranted(this) && PermissionHelper.isGPSOn(this)
                && mMap != null) {
            setupMap();
            renderUserLocation();
            startFollowingDevice();
            MapPresenter mapPresenter = new MapPresenter(this);
            mapPresenter.present(null,0);
            DirectionPresenter directionPresenter = new DirectionPresenter(this);
        }
    }

    // region IMapView...
    @Override
    public void renderForecasts(List<FloodForecastPopupFragment> forecasts) {
        mPopupFragments = forecasts;
        for(FloodForecastPopupFragment popupFragment : forecasts) {
//            Toast.makeText(this, popupFragment.getForecastLocation().toString(), Toast.LENGTH_LONG).show();
            popupFragment.renderForecastOnLocation(null);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void renderUserLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if(location != null)
                    MapHelper.moveCameraToLocation(mMap, location.getLatitude(),
                            location.getLongitude(), 15, false);
                else {
                    // Restart application to obtain device location
                    ProcessPhoenix.triggerRebirth(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void renderError(int errorCode, String errorString) {

    }
    // endregion

    // region MapActivity...
    private void styleMap() {

    }

    public GoogleMap getMap() {
        return mMap;
    }

    private void onActorMove() {

    }

    @SuppressLint({"MissingPermission", "PotentialBehaviorOverride"})
    private void setupMap() {
        mMap.setMyLocationEnabled(true);

        // Stop following device when the user moves the map
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    stopFollowingDevice();
                }
            }
        });

        // Start following device when the user click the button
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                startFollowingDevice();
                return false;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                FloodForecast.showDetailedFragment(mPopupFragments, marker, getSupportFragmentManager());
                stopFollowingDevice();
                return true;
            }
        });
    }

    private void onZoomRateChange() {

    }
    // endregion

    // region IDirectionView

    @Override
    public void renderPath(List<Location> path) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                List<LatLng> steps = new ArrayList<>();
                for(Location step: path){
                    steps.add(new LatLng(step.getLatitude(), step.getLongitude()));
                }
                Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(steps));
                polyline.setColor(getColor(R.color.colorPrimary));
                polyline.setClickable(true);
            }
        });
    }

    @Override
    public void renderPathFindingUnsuccessful() {

    }

    // endregion

    // region Misc...
    private void startFollowingDevice() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

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
                        apiException.startResolutionForResult(MapActivity.this, RC_GPS_SERVICE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    private void stopFollowingDevice() {
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
    // endregion

    // region Permissions...
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