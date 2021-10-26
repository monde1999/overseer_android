package com.adventurers.overseer.map.views;

import static com.adventurers.overseer.Constants.RC_ACCESS_FINE_LOCATION;
import static com.adventurers.overseer.Constants.RC_GPS_SERVICE;
import static com.adventurers.overseer.Constants.TAG_REPORT;
import static com.adventurers.overseer.Constants.RC_SEARCH_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.adventurers.overseer.R;
import com.adventurers.overseer.direction.models.Route;
import com.adventurers.overseer.direction.presenters.DirectionPresenter;
import com.adventurers.overseer.direction.views.IDirectionView;
import com.adventurers.overseer.floodforecast.views.FloodForecast;
import com.adventurers.overseer.floodforecast.views.FloodForecastPopupFragment;
import com.adventurers.overseer.helpers.DirectionHelper;
import com.adventurers.overseer.helpers.MapHelper;
import com.adventurers.overseer.helpers.PermissionHelper;
import com.adventurers.overseer.helpers.StatusBarHelper;
import com.adventurers.overseer.login.views.LoginActivity;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.presenters.MapPresenter;
import com.adventurers.overseer.report.views.ReportActivity;
import com.adventurers.overseer.searchtype.SearchType;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.button.MaterialButton;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks, IMapView, IDirectionView {
    private Location mFocusedLocation;
    private double mVisibilityRadius;
    private boolean mHazardVisibility;
    private GoogleMap mMap;
    private MapPresenter mMapPresenter;


    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean followUser;
    private Location userLocation;

    private List<FloodForecastPopupFragment> mPopupFragments;
    FloatingActionButton fab_report;
    private Location directionOrigin;
    private Location directionGoal;
    private DirectionHelper directionHelper;

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
        mMapPresenter = new MapPresenter(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        followUser = true;
        userLocation = new Location(0,0);
        fab_report = findViewById(R.id.map_fab_report);

        // Called when device location is updated
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(android.location.Location location : locationResult.getLocations()) {
                    if(followUser) {
                        MapHelper.moveCameraToLocation(mMap, location.getLatitude(), location.getLongitude(),
                                15, true);
                    }
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                    mMapPresenter.present(new Location(location.getLatitude(), location.getLongitude()), .5);
                }
                onActorMove();
            }
        };

        FloatingActionButton fab_logout = findViewById(R.id.map_fab_logout);
        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapActivity.this, LoginActivity.class);
                startActivity(i);
                stopLocationUpdates();
                MapActivity.this.finish();
            }
        });
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
        MapHelper.adjustMyLocationButton(this);
        PermissionHelper.ensureLocationAndGPS(this);
        startActivity();
    }

    @SuppressLint("MissingPermission")
    private void startActivity() {
        if(PermissionHelper.isLocationGranted(this) && PermissionHelper.isGPSOn(this)
                && mMap != null) {
            setupMap();
            renderUserLocation();
            runLocationUpdates();

            fab_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReportActivity reportFragment = ReportActivity.newInstance(userLocation);
                    reportFragment.show(getSupportFragmentManager(), TAG_REPORT);
                }
            });

            MaterialButton fab_search = findViewById(R.id.map_fab_search);
            fab_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchType.launch(MapActivity.this);
                }
            });
        }
    }


    // region IMapView...
    @Override
    public void renderForecasts(List<FloodForecastPopupFragment> forecasts) {
        // Remove previous popupFragments from the map
        if(mPopupFragments!=null) {
            for(FloodForecastPopupFragment popupFragment : mPopupFragments) {
                popupFragment.remove();
            }
        }

        // Render new popupFragments to the map
        mPopupFragments = forecasts;
        for(FloodForecastPopupFragment popupFragment : forecasts) {
            popupFragment.renderForecastOnLocation(null);
        }
        fab_report.setClickable(true);
        fab_report.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(52,152,219)));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void renderUserLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if(location != null) {
                            MapHelper.moveCameraToLocation(mMap, location.getLatitude(),
                                    location.getLongitude(), 15, false);
                        }
                        else {
                            // Restart application to obtain device location
                            ProcessPhoenix.triggerRebirth(MapActivity.this);
                        }
                    }
                });
    }

    @Override
    public void renderError(int errorCode, String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        fab_report.setClickable(false);
        fab_report.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.grey)));
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
                runLocationUpdates();
                return false;
            }
        });

        // Action when the user clicks on a marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                FloodForecast.showDetailedFragment(mPopupFragments, marker, getSupportFragmentManager());
                // Stop following device when the user clicks a marker
                stopFollowingDevice();
                return true;
            }
        });

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(@NonNull Polyline polyline) {
                directionHelper.selectPolyline(polyline);
//                polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
            }
        });
    }

    private void onZoomRateChange() {

    }
    // endregion

    // region SearchType...
    private void searchTypeSuccess(Place place) {
//        Toast.makeText(this, place.getAddress(), Toast.LENGTH_SHORT).show();
        DirectionPresenter directionPresenter = new DirectionPresenter(this);
        directionOrigin = new Location(userLocation.getLatitude(), userLocation.getLongitude());
        directionGoal = new Location(place.getLatLng().latitude, place.getLatLng().longitude);
        directionPresenter.present(directionOrigin, directionGoal);
    }
    // endregion

    // region IDirectionView

    @Override
    public void renderPaths(List<Route> routes) {
        directionHelper = new DirectionHelper(routes, directionOrigin, directionGoal, this);
        directionHelper.renderRoutes(mMap);
    }

    @Override
    public void renderPathFindingUnsuccessful() {
        Toast.makeText(this, "No routes found", Toast.LENGTH_SHORT).show();
    }
    // endregion

    // region Misc...
    private void runLocationUpdates() {
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
                if (e instanceof ResolvableApiException) {
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

    private void startFollowingDevice() {
        followUser = true;
    }

    private void stopFollowingDevice() {
        followUser = false;
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
        if(requestCode==RC_ACCESS_FINE_LOCATION || requestCode==RC_GPS_SERVICE) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        // Some permissions have been granted
        PermissionHelper.ensureLocationAndGPS(this);
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
            PermissionHelper.ensureLocationAndGPS(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen.
            PermissionHelper.ensureLocationAndGPS(this);
            startActivity();
        }
        else if(requestCode == RC_GPS_SERVICE) {
            // Do something after GPS is turned on in location dialog
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PermissionHelper.ensureLocationAndGPS(this);
                    startActivity();
                    break;
                case Activity.RESULT_CANCELED:
                    PermissionHelper.ensureLocationAndGPS(this);
                    break;
            }
        }
        else if(requestCode == RC_SEARCH_TYPE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            searchTypeSuccess(place);
        }
    }
    // endregion
}