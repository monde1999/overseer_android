package com.adventurers.overseer.navigation;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.mapbox.api.directions.v5.DirectionsCriteria.OVERVIEW_FULL;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.adventurers.overseer.R;
import com.adventurers.overseer.api.FloodArea;
import com.adventurers.overseer.api.OverseerApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.ui.IconGenerator;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.Point;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.navigation.base.TimeFormat;
import com.mapbox.navigation.base.extensions.RouteOptionsExtensions;
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.RouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.base.trip.model.RouteProgress;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.MapboxNavigationProvider;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter;
import com.mapbox.navigation.core.replay.MapboxReplayer;
import com.mapbox.navigation.core.replay.ReplayLocationEngine;
import com.mapbox.navigation.core.replay.history.ReplayEventBase;
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver;
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.core.trip.session.RouteProgressObserver;
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi;
import com.mapbox.navigation.ui.maneuver.model.Maneuver;
import com.mapbox.navigation.ui.maneuver.model.ManeuverError;
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView;
import com.mapbox.navigation.ui.maps.camera.NavigationCamera;
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource;
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler;
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState;
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraStateChangedObserver;
import com.mapbox.navigation.ui.maps.camera.transition.MapboxNavigationCameraStateTransition;
import com.mapbox.navigation.ui.maps.camera.transition.MapboxNavigationCameraTransition;
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransition;
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions;
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton;
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi;
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView;
import com.mapbox.navigation.ui.maps.route.arrow.model.InvalidPointError;
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions;
import com.mapbox.navigation.ui.maps.route.arrow.model.UpdateManeuverArrowValue;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineClearValue;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi;
import com.mapbox.navigation.ui.tripprogress.model.DistanceRemainingFormatter;
import com.mapbox.navigation.ui.tripprogress.model.EstimatedTimeToArrivalFormatter;
import com.mapbox.navigation.ui.tripprogress.model.PercentDistanceTraveledFormatter;
import com.mapbox.navigation.ui.tripprogress.model.TimeRemainingFormatter;
import com.mapbox.navigation.ui.tripprogress.model.TripProgressUpdateFormatter;
import com.mapbox.navigation.ui.tripprogress.view.MapboxTripProgressView;
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi;
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer;
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement;
import com.mapbox.navigation.ui.voice.model.SpeechError;
import com.mapbox.navigation.ui.voice.model.SpeechValue;
import com.mapbox.navigation.ui.voice.model.SpeechVolume;
import com.mapbox.navigation.ui.voice.view.MapboxSoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity {
    // region Variables...
    private static final Long BUTTON_ANIMATION_DURATION = 1500L;

    /**
     * Debug tool used to play, pause and seek route progress events that can be used to produce mocked location updates along the route.
     */
    private final MapboxReplayer mapboxReplayer = new MapboxReplayer();

    /**
     * Debug tool that mocks location updates with an input from the [mapboxReplayer].
     */
    private final ReplayLocationEngine replayLocationEngine = new ReplayLocationEngine(mapboxReplayer);

    /**
     * Debug observer that makes sure the replayer has always an up-to-date information to generate mock updates.
     */
    private final ReplayProgressObserver replayProgressObserver = new ReplayProgressObserver(mapboxReplayer);

    // Layout components
    private MapView mapView;
    private CardView tripProgressCard;
    private MapboxTripProgressView tripProgressView;
    private ImageView stop;
    private MapboxManeuverView maneuverView;
    private MapboxSoundButton soundButton;
    private MapboxRouteOverviewButton routeOverview;
    private MapboxRecenterButton recenter;

    /**
     * Mapbox Maps entry point obtained from the [MapView].
     * You need to get a new reference to this object whenever the [MapView] is recreated.
     */
    private MapboxMap mapboxMap;

    /**
     * Mapbox Navigation entry point. There should only be one instance of this object for the app.
     * You can use [MapboxNavigationProvider] to help create and obtain that instance.
     */
    private MapboxNavigation mapboxNavigation;

    /**
     * Used to execute camera transitions based on the data generated by the [viewportDataSource].
     * This includes transitions from route overview to route following and continuously updating the camera as the location changes.
     */
    private NavigationCamera navigationCamera;

    /**
     * Produces the camera frames based on the location and routing data for the [navigationCamera] to execute.
     */

    /*
     * Below are generated camera padding values to ensure that the route fits well on screen while
     * other elements are overlaid on top of the map (including instruction view, buttons, etc.)
     */
    private MapboxNavigationViewportDataSource viewportDataSource;
    private final float pixelDensity = Resources.getSystem().getDisplayMetrics().density;
    private final EdgeInsets overviewPadding =
            new EdgeInsets(
                    140.0 * pixelDensity,
                    40.0 * pixelDensity,
                    120.0 * pixelDensity,
                    40.0 * pixelDensity
            );
    private final EdgeInsets landscapeOverviewPadding =
            new EdgeInsets(
                    30.0 * pixelDensity,
                    380.0 * pixelDensity,
                    110.0 * pixelDensity,
                    20.0 * pixelDensity
            );
    private final EdgeInsets followingPadding =
            new EdgeInsets(
                    180.0 * pixelDensity,
                    40.0 * pixelDensity,
                    150.0 * pixelDensity,
                    40.0 * pixelDensity
            );
    private final EdgeInsets landscapeFollowingPadding =
            new EdgeInsets(
                    30.0 * pixelDensity,
                    380.0 * pixelDensity,
                    110.0 * pixelDensity,
                    40.0 * pixelDensity
            );

    /**
     * Generates updates for the [MapboxManeuverView] to display the upcoming maneuver instructions
     * and remaining distance to the maneuver point.
     */
    private MapboxManeuverApi maneuverApi;

    /**
     * Generates updates for the [MapboxTripProgressView] that include remaining time and distance to the destination.
     */
    private MapboxTripProgressApi tripProgressApi;

    /**
     * Generates updates for the [routeLineView] with the geometries and properties of the routes that should be drawn on the map.
     */
    private MapboxRouteLineApi routeLineApi;

    /**
     * Draws route lines on the map based on the data from the [routeLineApi]
     */
    private MapboxRouteLineView routeLineView;

    /**
     * Generates updates for the [routeArrowView] with the geometries and properties of maneuver arrows that should be drawn on the map.
     */
    private final MapboxRouteArrowApi routeArrowApi = new MapboxRouteArrowApi();

    /**
     * Draws maneuver arrows on the map based on the data [routeArrowApi].
     */
    private MapboxRouteArrowView routeArrowView;

    /**
     * Stores and updates the state of whether the voice instructions should be played as they come or muted.
     */
    private boolean isVoiceInstructionsMuted = false;
    private void setVoiceInstructionsMuted(boolean voiceInstructionsMuted) {
        isVoiceInstructionsMuted = voiceInstructionsMuted;
        if (voiceInstructionsMuted) {
            soundButton.muteAndExtend(BUTTON_ANIMATION_DURATION);
            voiceInstructionsPlayer.volume(new SpeechVolume(0f));
        } else {
            soundButton.unmuteAndExtend(BUTTON_ANIMATION_DURATION);
            voiceInstructionsPlayer.volume(new SpeechVolume(1f));
        }
    }

    /**
     * Extracts message that should be communicated to the driver about the upcoming maneuver.
     * When possible, downloads a synthesized audio file that can be played back to the driver.
     */
    private MapboxSpeechApi speechApi;

    /**
     * Plays the synthesized audio files with upcoming maneuver instructions
     * or uses an on-device Text-To-Speech engine to communicate the message to the driver.
     */
    private MapboxVoiceInstructionsPlayer voiceInstructionsPlayer;

    /**
     * Observes when a new voice instruction should be played.
     */
    private final VoiceInstructionsObserver voiceInstructionsObserver = new VoiceInstructionsObserver() {
        @Override
        public void onNewVoiceInstructions(@NonNull VoiceInstructions voiceInstructions) {
            speechApi.generate(voiceInstructions, speechCallback);
        }
    };

    /**
     * Based on whether the synthesized audio file is available, the callback plays the file
     * or uses the fall back which is played back using the on-device Text-To-Speech engine.
     */
    private final MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> speechCallback =
            new MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>>() {
                @Override
                public void accept(Expected<SpeechError, SpeechValue> speechErrorSpeechValueExpected) {
                    speechErrorSpeechValueExpected.fold(
                            new Expected.Transformer<SpeechError, Object>() {
                                @NonNull
                                @Override
                                public Object invoke(@NonNull SpeechError input) {
                                    // play the instruction via fallback text-to-speech engine
                                    voiceInstructionsPlayer.play(
                                            input.getFallback(),
                                            voiceInstructionsPlayerCallback
                                    );
                                    return null;
                                }
                            },
                            new Expected.Transformer<SpeechValue, Object>() {
                                @NonNull
                                @Override
                                public Object invoke(@NonNull SpeechValue input) {
                                    // play the sound file from the external generator
                                    voiceInstructionsPlayer.play(
                                            input.getAnnouncement(),
                                            voiceInstructionsPlayerCallback
                                    );
                                    return null;
                                }
                            }
                    );
                }
            };

    /**
     * When a synthesized audio file was downloaded, this callback cleans up the disk after it was played.
     */
    private final MapboxNavigationConsumer<SpeechAnnouncement> voiceInstructionsPlayerCallback =
            new MapboxNavigationConsumer<SpeechAnnouncement>() {
                @Override
                public void accept(SpeechAnnouncement speechAnnouncement) {
                    // remove already consumed file to free-up space
                    speechApi.clean(speechAnnouncement);
                }
            };

    /**
     * [NavigationLocationProvider] is a utility class that helps to provide location updates generated by the Navigation SDK
     * to the Maps SDK in order to update the user location indicator on the map.
     */
    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();

    /**
     * Gets notified with location updates.
     *
     * Exposes raw updates coming directly from the location services
     * and the updates enhanced by the Navigation SDK (cleaned up and matched to the road).
     */
    private final LocationObserver locationObserver = new LocationObserver() {
        boolean firstLocationUpdateReceived = false;

        @Override
        public void onNewRawLocation(@NonNull Location location) {
            // not handled
        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location enhancedLocation = locationMatcherResult.getEnhancedLocation();
            // update location puck's position on the map
            navigationLocationProvider.changePosition(
                    enhancedLocation,
                    locationMatcherResult.getKeyPoints(), null, null
            );

            // update camera position to account for new location
            viewportDataSource.onLocationChanged(enhancedLocation);
            viewportDataSource.evaluate();

            // if this is the first location update the activity has received,
            // it's best to immediately move the camera to the current user location
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true;
                navigationCamera.requestNavigationCameraToOverview(
                        new NavigationCameraTransitionOptions.Builder()
                                .maxDuration(0) // instant transition
                                .build()
                );
            }
            startFloodPointsUpdates(enhancedLocation.getLatitude(), enhancedLocation.getLongitude());
        }
    };

    /**
     * Gets notified with progress along the currently active route.
     */
    private final RouteProgressObserver routeProgressObserver = new RouteProgressObserver() {
        @Override
        public void onRouteProgressChanged(@NonNull RouteProgress routeProgress) {
            // update the camera position to account for the progressed fragment of the route
            viewportDataSource.onRouteProgressChanged(routeProgress);
            viewportDataSource.evaluate();

            // draw the upcoming maneuver arrow on the map
            Style style = mapboxMap.getStyle();
            if (style != null) {
                Expected<InvalidPointError, UpdateManeuverArrowValue> maneuverArrowResult =
                        routeArrowApi.addUpcomingManeuverArrow(routeProgress);
                routeArrowView.renderManeuverUpdate(style, maneuverArrowResult);
            }

            // update top banner with maneuver instructions
            Expected<ManeuverError, List<Maneuver>> maneuvers = maneuverApi.getManeuvers(routeProgress);
            maneuvers.fold(
                    new Expected.Transformer<ManeuverError, Object>() {

                        @NonNull
                        @Override
                        public Object invoke(@NonNull ManeuverError input) {
                            Toast.makeText(
                                    NavigationActivity.this,
                                    input.getErrorMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            return null;
                        }
                    },
                    new Expected.Transformer<List<Maneuver>, Object>() {
                        @NonNull
                        @Override
                        public Object invoke(@NonNull List<Maneuver> input) {
                            maneuverView.setVisibility(View.VISIBLE);
                            maneuverView.renderManeuvers(maneuvers);
                            return null;
                        }
                    }
            );
            // update bottom trip progress summary
            tripProgressView.render(
                    tripProgressApi.getTripProgress(routeProgress)
            );
        }
    };

    /**
     * Gets notified whenever the tracked routes change.
     *
     * A change can mean:
     * - routes get changed with [MapboxNavigation.setRoutes]
     * - routes annotations get refreshed (for example, congestion annotation that indicate the live traffic along the route)
     * - driver got off route and a reroute was executed
     */
    private final RoutesObserver routesObserver = new RoutesObserver() {
        @Override
        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
            if (!routesUpdatedResult.getRoutes().isEmpty()) {
                // generate route geometries asynchronously and render them
                List<RouteLine> routeLines = new ArrayList<>();
                for (DirectionsRoute route : routesUpdatedResult.getRoutes()) {
                    routeLines.add(new RouteLine(route, null));
                }
                routeLineApi.setRoutes(
                        routeLines,
                        new MapboxNavigationConsumer<Expected<RouteLineError, RouteSetValue>>() {
                            @Override
                            public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
                                if (mapboxMap.getStyle() != null) {
                                    routeLineView.renderRouteDrawData(mapboxMap.getStyle(), routeLineErrorRouteSetValueExpected);
                                }
                            }
                        }
                );

                // update the camera position to account for the new route
                viewportDataSource.onRouteChanged(routesUpdatedResult.getRoutes().get(0));
                viewportDataSource.evaluate();
            } else {
                // remove the route line and route arrow from the map
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    routeLineApi.clearRouteLine(
                            new MapboxNavigationConsumer<Expected<RouteLineError, RouteLineClearValue>>() {
                                @Override
                                public void accept(Expected<RouteLineError, RouteLineClearValue> routeLineErrorRouteLineClearValueExpected) {
                                    routeLineView.renderClearRouteLineValue(
                                            style,
                                            routeLineErrorRouteLineClearValueExpected
                                    );
                                }
                            }
                    );
                    routeArrowView.render(style, routeArrowApi.clearArrows());
                }

                // remove the route reference from camera position evaluations
                viewportDataSource.clearRouteData();
                viewportDataSource.evaluate();
            }
        }
    };

    // endregion
    AnnotationPlugin annotationApi;
    PointAnnotationManager pointAnnotationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        mapView = findViewById(R.id.mapView);
        tripProgressCard = findViewById(R.id.tripProgressCard);
        tripProgressView = findViewById(R.id.tripProgressView);
        stop = findViewById(R.id.stop);
        maneuverView = findViewById(R.id.maneuverView);
        soundButton = findViewById(R.id.soundButton);
        routeOverview = findViewById(R.id.routeOverview);
        recenter = findViewById(R.id.recenter);
        mapboxMap = mapView.getMapboxMap();
        annotationApi = mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
        if (annotationApi != null) {
            pointAnnotationManager =
                    (PointAnnotationManager) annotationApi.createAnnotationManager(mapView, AnnotationType.PointAnnotation, new AnnotationConfig());
        }

        // Retrieve route from intent
        ArrayList<String> sRoute = getIntent().getStringArrayListExtra("Route");
        ArrayList<Point> gRoute = new ArrayList<>();
        Gson gson = new Gson();
        for(String s : sRoute) {
            LatLng latLng = gson.fromJson(s, LatLng.class);
            gRoute.add(Point.fromLngLat(latLng.longitude, latLng.latitude));
        }

        // initialize the location puck
        LocationComponentPlugin locationComponent =
                mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);
        if (locationComponent != null) {
            locationComponent.setLocationPuck(new LocationPuck2D(
                    ContextCompat.getDrawable(
                            NavigationActivity.this,
                            R.drawable.mapbox_navigation_puck_icon
                    )
            ));
            locationComponent.setLocationProvider(navigationLocationProvider);
            locationComponent.setEnabled(true);
        }

        // initialize Mapbox Navigation
        if (MapboxNavigationProvider.isCreated()) {
            mapboxNavigation = MapboxNavigationProvider.retrieve();
        } else {
            mapboxNavigation = MapboxNavigationProvider.create(
                    new NavigationOptions.Builder(NavigationActivity.this)
                            .accessToken(getString(R.string.mapbox_access_token))
                            // comment out the location engine setting block to disable simulation
                            .locationEngine(replayLocationEngine)
                            .build()
            );
        }

        // initialize Navigation Camera
        viewportDataSource = new MapboxNavigationViewportDataSource(mapboxMap);
        CameraAnimationsPlugin cameraPlugin = mapView.getPlugin(Plugin.MAPBOX_CAMERA_PLUGIN_ID);
        NavigationCameraTransition navigationCameraTransition =
                new MapboxNavigationCameraTransition(mapboxMap, cameraPlugin);
        MapboxNavigationCameraStateTransition navigationCameraStateTransition =
                new MapboxNavigationCameraStateTransition(mapboxMap, cameraPlugin, navigationCameraTransition);
        navigationCamera = new NavigationCamera(
                mapboxMap,
                cameraPlugin,
                viewportDataSource,
                navigationCameraStateTransition

        );
        // set the animations lifecycle listener to ensure the NavigationCamera stops
        // automatically following the user location when the map is interacted with
        cameraPlugin.addCameraAnimationsLifecycleListener(
                new NavigationBasicGesturesHandler(navigationCamera)
        );
        navigationCamera.registerNavigationCameraStateChangeObserver(new NavigationCameraStateChangedObserver() {
            @Override
            public void onNavigationCameraStateChanged(@NonNull NavigationCameraState navigationCameraState) {
                // shows/hide the recenter button depending on the camera state
                switch (navigationCameraState) {
                    case TRANSITION_TO_FOLLOWING:
                    case FOLLOWING:
                        recenter.setVisibility(View.INVISIBLE);
                        break;
                    case TRANSITION_TO_OVERVIEW:
                    case OVERVIEW:
                    case IDLE:
                        recenter.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        // set the padding values depending on screen orientation and visible view layout
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.setOverviewPadding(landscapeOverviewPadding);
        } else {
            viewportDataSource.setOverviewPadding(overviewPadding);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.setFollowingPadding(landscapeFollowingPadding);
        } else {
            viewportDataSource.setFollowingPadding(followingPadding);
        }

        // make sure to use the same DistanceFormatterOptions across different features
        DistanceFormatterOptions distanceFormatterOptions = mapboxNavigation.getNavigationOptions().getDistanceFormatterOptions();

        // initialize maneuver api that feeds the data to the top banner maneuver view
        maneuverApi = new MapboxManeuverApi(
                new MapboxDistanceFormatter(distanceFormatterOptions)
        );

        // initialize bottom progress view
        tripProgressApi = new MapboxTripProgressApi(
                new TripProgressUpdateFormatter.Builder(this)
                        .distanceRemainingFormatter(
                                new DistanceRemainingFormatter(distanceFormatterOptions)
                        )
                        .timeRemainingFormatter(
                                new TimeRemainingFormatter(this, null)
                        )
                        .percentRouteTraveledFormatter(
                                new PercentDistanceTraveledFormatter()
                        )
                        .estimatedTimeToArrivalFormatter(
                                new EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
                        )
                        .build()
        );

        // initialize voice instructions api and the voice instruction player
        speechApi = new MapboxSpeechApi(
                this,
                getString(R.string.mapbox_access_token),
                Locale.US.getLanguage()
        );
        voiceInstructionsPlayer = new MapboxVoiceInstructionsPlayer(
                this,
                getString(R.string.mapbox_access_token),
                Locale.US.getLanguage()
        );

        // initialize route line, the withRouteLineBelowLayerId is specified to place
        // the route line below road labels layer on the map
        // the value of this option will depend on the style that you are using
        // and under which layer the route line should be placed on the map layers stack
        MapboxRouteLineOptions mapboxRouteLineOptions = new MapboxRouteLineOptions.Builder(this)
                .withRouteLineBelowLayerId("road-label")
                .build();
        routeLineApi = new MapboxRouteLineApi(mapboxRouteLineOptions);
        routeLineView = new MapboxRouteLineView(mapboxRouteLineOptions);

        // initialize maneuver arrow view to draw arrows on the map
        RouteArrowOptions routeArrowOptions = new RouteArrowOptions.Builder(this).build();
        routeArrowView = new MapboxRouteArrowView(routeArrowOptions);

        // load map style
        mapboxMap.loadStyleUri(
                Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
//                        GesturesPlugin gesture = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
//                        gesture.addOnMapLongClickListener(new OnMapLongClickListener() {
//                            @Override
//                            public boolean onMapLongClick(@NonNull Point point) {
//                                //findRoute(point);
//                                //startFloodPointsUpdates();
//                                //showFloodPoints(null);
//                                return true;
//                            }
//                        });
                        snapRoute(gRoute);
                    }
                }
        );

        // initialize view interactions
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRouteAndStopNavigation();
            }
        });
        recenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationCamera.requestNavigationCameraToFollowing();
                routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION);
            }
        });
        routeOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationCamera.requestNavigationCameraToOverview();
                recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION);
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mute/unmute voice instructions
                setVoiceInstructionsMuted(!isVoiceInstructionsMuted);
            }
        });

        // set initial sounds button state
        soundButton.unmute();

        // start the trip session to being receiving location updates in free drive
        // and later when a route is set also receiving route progress updates
        mapboxNavigation.startTripSession();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // register event listeners
        mapboxNavigation.registerRoutesObserver(routesObserver);
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver);
        mapboxNavigation.registerLocationObserver(locationObserver);
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver);
        mapboxNavigation.registerRouteProgressObserver(replayProgressObserver);

//        if (mapboxNavigation.getRoutes().isEmpty()) {
//            // if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
//            // but we're not simulating yet,
//            // push a single location sample to establish origin
//            mapboxReplayer.pushEvents(
//                    Collections.singletonList(
//                            ReplayRouteMapper.mapToUpdateLocation(
//                                    0.0,
//                                    // Point.fromLngLat(123.746122, 10.194461)
//                            )
//                    )
//            );
//        }
//        mapboxReplayer.playFirstLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unregister event listeners to prevent leaks or unnecessary resource consumption
        mapboxNavigation.unregisterRoutesObserver(routesObserver);
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver);
        mapboxNavigation.unregisterLocationObserver(locationObserver);
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver);
        mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapboxNavigationProvider.destroy();
        speechApi.cancel();
        voiceInstructionsPlayer.shutdown();
    }

    private void findRoute(Point destination) {
        Location originLocation = navigationLocationProvider.getLastLocation();
        if(originLocation==null) return;
        Point originPoint = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        RouteOptions.Builder routeOptionsBuilder = RouteOptions.builder()
                .coordinatesList(Arrays.asList(originPoint, destination))
                // provide the bearing for the origin of the request to ensure
                // that the returned route faces in the direction of the current user movement
                .bearingsList(
                        Arrays.asList(
                                Bearing.builder()
                                        .angle(originLocation.getBearing())
                                        .degrees(45.0)
                                        .build(), null
                        )
                );
        RouteOptionsExtensions.applyDefaultNavigationOptions(routeOptionsBuilder);
        RouteOptionsExtensions.applyLanguageAndVoiceUnitOptions(routeOptionsBuilder, this);
        mapboxNavigation.requestRoutes(
                routeOptionsBuilder.build(),
                new RouterCallback() {
                    @Override
                    public void onRoutesReady(@NonNull List<? extends DirectionsRoute> list, @NonNull RouterOrigin routerOrigin) {
                        List<DirectionsRoute> routes = new ArrayList<>(list);
                        setRouteAndStartNavigation(routes);
                    }

                    @Override
                    public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                        // no impl
                    }

                    @Override
                    public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {
                        // no impl
                    }
                }
        );
    }

    private void setRouteAndStartNavigation(List<DirectionsRoute> routes) {
        // set routes, where the first route in the list is the primary route that
        // will be used for active guidance
        mapboxNavigation.setRoutes(routes);

        // start location simulation along the primary route
        startSimulation(routes.get(0));

        // show UI elements
        soundButton.setVisibility(View.VISIBLE);
        routeOverview.setVisibility(View.VISIBLE);
        tripProgressCard.setVisibility(View.VISIBLE);

        // move the camera to overview when new route is available
        // navigationCamera.requestNavigationCameraToOverview();

        // move the camera to following when new route is available
        recenter.callOnClick();
    }

    private void clearRouteAndStopNavigation() {
        // clear
        mapboxNavigation.setRoutes(Collections.emptyList());

        // stop simulation
        mapboxReplayer.stop();

        // hide UI elements
        soundButton.setVisibility(View.INVISIBLE);
        maneuverView.setVisibility(View.INVISIBLE);
        routeOverview.setVisibility(View.INVISIBLE);
        tripProgressCard.setVisibility(View.INVISIBLE);
    }

    private void startSimulation(DirectionsRoute route) {
        mapboxReplayer.stop();
        mapboxReplayer.clearEvents();
        List<ReplayEventBase> replayEvents = new ReplayRouteMapper().mapDirectionsRouteGeometry(route);
        mapboxReplayer.pushEvents(replayEvents);
        mapboxReplayer.seekTo(replayEvents.get(0));
        mapboxReplayer.play();
    }

    private void snapRoute(ArrayList<Point> gRoute) {
        // Remove random points since the API limit is only 100 points
        if(gRoute.size()>100) {
            int mustRemove = gRoute.size() - 98;

            // Generate random numbers except first and last
            List<Integer> xPointIndex = new ArrayList<>();
            for (int i=1; i<gRoute.size()-1; i++) {
                xPointIndex.add(i);
            }
            Collections.shuffle(xPointIndex);

            // Remove random points
            ArrayList<Point> xPoints = new ArrayList<>();
            for (int i=0; i<mustRemove; i++) {
                xPoints.add(gRoute.get(xPointIndex.get(i)));
            }
            gRoute.removeAll(xPoints);
        }

        MapboxMapMatching mapboxMapMatching = MapboxMapMatching.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .coordinates(gRoute)
                .steps(true)
                .voiceInstructions(true)
                .bannerInstructions(true)
                .profile(PROFILE_DRIVING)
                .overview(OVERVIEW_FULL)
                .waypointIndices(0, gRoute.size() - 1)
                .build();
        mapboxMapMatching.enqueueCall(new Callback<MapMatchingResponse>() {
            @Override
            public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<MapMatchingMatching> mapMatchingMatchings = response.body().matchings();
                        if (mapMatchingMatchings != null) {
                            DirectionsRoute directionRoute = mapMatchingMatchings.get(0).toDirectionRoute();
                            setRouteAndStartNavigation(Collections.singletonList(directionRoute));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MapMatchingResponse> call, Throwable t) {

            }
        });
    }

    private void showFloodPoints(List<FloodArea> floodAreas) {
        pointAnnotationManager.deleteAll();
        IconGenerator mIconGenerator = new IconGenerator(this);
        mIconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
        PointAnnotationOptions pointAnnotationOptions;
        for (FloodArea floodArea : floodAreas) {
            pointAnnotationOptions = new PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(floodArea.getLongitude(), floodArea.getLatitude()))
                    .withIconImage(mIconGenerator.makeIcon("Flood"));
            pointAnnotationManager.create(pointAnnotationOptions);
        }
    }

    private Bitmap bitmapFromDrawableRes(Context context, int resourceId) {
        return convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId));
    }

    private Bitmap convertDrawableToBitmap(Drawable sourceDrawable) {
        if (sourceDrawable == null) {
            return null;
        }
        if (sourceDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) sourceDrawable).getBitmap();
        } else {
            // copying drawable object to not manipulate on the same reference
            Drawable.ConstantState constantState = sourceDrawable.getConstantState();
            Drawable drawable = constantState.newDrawable().mutate();
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    private void startFloodPointsUpdates(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<FloodArea>> call = overseerApi.getFloodAreas(latitude, longitude);
        call.enqueue(new Callback<List<FloodArea>>() {
            @Override
            public void onResponse(Call<List<FloodArea>> call, Response<List<FloodArea>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    showFloodPoints(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<FloodArea>> call, Throwable t) {

            }
        });
    }
}