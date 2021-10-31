//package com.adventurers.overseer.navigation;
//
//import android.annotation.SuppressLint;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.adventurers.overseer.R;
//import com.adventurers.overseer.helpers.StatusBarHelper;
//import com.mapbox.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.api.directions.v5.models.VoiceInstructions;
//import com.mapbox.bindgen.Expected;
//import com.mapbox.mapboxsdk.location.LocationComponent;
//import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
//import com.mapbox.mapboxsdk.location.modes.CameraMode;
//import com.mapbox.mapboxsdk.location.modes.RenderMode;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.maps.MapView;
//import com.mapbox.maps.MapboxMap;
//import com.mapbox.maps.plugin.Plugin;
//import com.mapbox.navigation.core.MapboxNavigation;
//import com.mapbox.navigation.core.directions.session.RoutesObserver;
//import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
//import com.mapbox.navigation.core.replay.MapboxReplayer;
//import com.mapbox.navigation.core.replay.ReplayLocationEngine;
//import com.mapbox.navigation.core.replay.route.ReplayProgressObserver;
//import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
//import com.mapbox.navigation.core.trip.session.LocationObserver;
//import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver;
//import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
//import com.mapbox.navigation.ui.maps.camera.NavigationCamera;
//import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource;
//import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions;
//import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
//import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
//import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
//import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
//import com.mapbox.navigation.ui.maps.route.line.model.RouteLine;
//import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
//import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
//import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
//import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi;
//import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer;
//import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement;
//import com.mapbox.navigation.ui.voice.model.SpeechError;
//import com.mapbox.navigation.ui.voice.model.SpeechValue;
//import com.mapbox.navigation.ui.voice.model.SpeechVolume;
//import com.mapbox.navigation.ui.voice.view.MapboxSoundButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NavigationActivity extends AppCompatActivity {
//    private final DirectionsRoute route = DirectionsRoute.fromJson(getString(R.string.fix_route));
//    private final MapboxReplayer mapboxReplayer = new MapboxReplayer();
//    private final ReplayLocationEngine replayLocationEngine = new ReplayLocationEngine(mapboxReplayer);
//    private final ReplayProgressObserver replayProgressObserver = new ReplayProgressObserver(mapboxReplayer);
//    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();
//    private MapboxNavigation mapboxNavigation;
//    private MapboxMap mapboxMap;
//    private NavigationCamera navigationCamera;
//    private MapboxNavigationViewportDataSource viewportDataSource;
//    private final MapboxSoundButton soundButton = findViewById(R.id.soundButton);
//    private final MapView mapView = findViewById(R.id.mapView);
//    private MapboxSpeechApi speechApi;
//    private MapboxVoiceInstructionsPlayer voiceInstructionsPlayer;
//    private boolean isVoiceInstructionsMuted = false;
//    private void setIsVoiceInstructed(boolean isVoiceInstructedMuted) {
//        this.isVoiceInstructionsMuted = isVoiceInstructedMuted;
//        if (isVoiceInstructedMuted) {
//            soundButton.muteAndExtend(1500L);
//            voiceInstructionsPlayer.volume(new SpeechVolume(0f));
//        } else {
//            soundButton.unmuteAndExtend(1500L);
//            voiceInstructionsPlayer.volume(new SpeechVolume(1f));
//        }
//    }
//    private final MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(this)
//            .withRouteLineResources(new RouteLineResources.Builder().build())
//            .withRouteLineBelowLayerId("road-label")
//            .build();
//    private final MapboxRouteLineView routeLineView = new MapboxRouteLineView(options);
//    private final MapboxRouteLineApi routeLineApi = new MapboxRouteLineApi(options);
//    private final MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> speechCallback =
//            new MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>>() {
//                @Override
//                public void accept(Expected<SpeechError, SpeechValue> expected) {
//                    expected.fold(
//                            new Expected.Transformer<SpeechError, Object>() {
//                                @NonNull
//                                @Override
//                                public Object invoke(@NonNull SpeechError input) {
//                                    Log.d("abhishek_testing", "speechCallback: $error");
//                                    // play the instruction via fallback text-to-speech engine
//                                    voiceInstructionsPlayer.play(
//                                            input.getFallback(),
//                                            voiceInstructionsPlayerCallback
//                                    );
//                                    return null;
//                                }
//                            },
//                            new Expected.Transformer<SpeechValue, Object>() {
//                                @NonNull
//                                @Override
//                                public Object invoke(@NonNull SpeechValue input) {
//                                    Log.d("abhishek_testing", "speechCallback: $error");
//                                    // play the sound file from the external generator
//                                    voiceInstructionsPlayer.play(
//                                            input.getAnnouncement(),
//                                            voiceInstructionsPlayerCallback
//                                    );
//                                    return null;
//                                }
//                            }
//                    );
//                }
//            };
//    private final MapboxNavigationConsumer<SpeechAnnouncement> voiceInstructionsPlayerCallback =
//            new MapboxNavigationConsumer<SpeechAnnouncement>() {
//                @Override
//                public void accept(SpeechAnnouncement speechAnnouncement) {
//                    speechApi.clean(speechAnnouncement);
//                }
//            };
//    private final LocationObserver locationObserver = new LocationObserver() {
//        boolean firstLocationUpdateReceived = false;
//        @Override
//        public void onNewRawLocation(@NonNull Location location) {
//
//        }
//
//        @Override
//        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
//            Location enhancedLocation = locationMatcherResult.getEnhancedLocation();
//            navigationLocationProvider.changePosition(
//                    enhancedLocation,
//                    locationMatcherResult.getKeyPoints(), null, null
//                    );
//
//            viewportDataSource.onLocationChanged(enhancedLocation);
//            viewportDataSource.evaluate();
//
//            if (!firstLocationUpdateReceived) {
//                firstLocationUpdateReceived = true;
//                navigationCamera.requestNavigationCameraToOverview(
//                        new NavigationCameraTransitionOptions.Builder()
//                                .maxDuration(0) // instant transition
//                                .build()
//                );
//            }
//        }
//    };
//    private final RoutesObserver routesObserver = new RoutesObserver() {
//        @Override
//        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
//            List<DirectionsRoute> directionsRoutes = routesUpdatedResult.getRoutes();
//            List<RouteLine> routeLines = new ArrayList<>();
//            for (DirectionsRoute route : directionsRoutes) {
//                routeLines.add(new RouteLine(route, null));
//            }
//            routeLineApi.setRoutes(routeLines, new MapboxNavigationConsumer<Expected<RouteLineError, RouteSetValue>>() {
//                @Override
//                public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
//                    if(mapboxMap.getStyle()!=null) {
//                        routeLineView.renderRouteDrawData(mapboxMap.getStyle(), routeLineErrorRouteSetValueExpected);
//                    }
//                }
//            });
//        }
//    };
//    private final VoiceInstructionsObserver voiceInstructionsObserver = new VoiceInstructionsObserver() {
//        @Override
//        public void onNewVoiceInstructions(@NonNull VoiceInstructions voiceInstructions) {
//            Log.d("abhishek_testing", "voiceInstructionsObserver: $voiceInstructions");
//            speechApi.generate(voiceInstructions, speechCallback);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_navigation);
//        mapboxMap = mapView.getMapboxMap();
//        StatusBarHelper.makeTransparent(this);
//
//        mapView.createPlugin();
//
////        MapboxMapMatching mapboxMapMatching = MapboxMapMatching.builder()
////                .accessToken(getString(R.string.mapbox_access_token))
////                .profile(PROFILE_DRIVING)
////                .coordinates(getRouteCoordinates())
////                .overview(OVERVIEW_FULL)
////                .steps(false)
////                .build();
////
////        mapboxMapMatching.enqueueCall(new Callback<MapMatchingResponse>() {
////            @Override
////            public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
////                if(!response.isSuccessful()){
////                    return;
////                }
////                if (response.body() != null && response.body().matchings()!=null) {
////                    DirectionsRoute route = response.body().matchings().get(0).toDirectionRoute();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<MapMatchingResponse> call, Throwable t) {
////
////            }
////        });
//
//
//    }
//
//    public void setVoiceInstructionsMuted(boolean voiceInstructionsMuted) {
//
//    }
//
////    private List<Point> getRouteCoordinates() {
////        List<Point> route = new ArrayList<>();
////        for(LatLng latLng : googleRoute) {
////            route.add(Point.fromLngLat(latLng.lat, latLng.lng));
////        }
////        return route;
////    }
//
//    @Override
//    public void onMapReady(@NonNull MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//
//        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/streets-v11"),
//                new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//                        enableLocationComponent(style);
//                    }
//                });
//    }
//
//    @SuppressLint("MissingPermission")
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//        // Get an instance of the component
//        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//        // Activate with options
//        locationComponent.activateLocationComponent(
//                LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
//
//        // Enable to make component visible
//        locationComponent.setLocationComponentEnabled(true);
//
//        // Set the component's camera mode
//        locationComponent.setCameraMode(CameraMode.TRACKING);
//
//        // Set the component's render mode
//        locationComponent.setRenderMode(RenderMode.COMPASS);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//}