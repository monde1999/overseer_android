package com.adventurers.overseer.floodforecast.views;

import android.content.Context;

import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class FloodForecastPopupFragment implements IFloodForecastView {
    private final Location mForecastLocation;
    private IconGenerator iconGenerator;
    private Marker marker;
    private GoogleMap mMap;

    public FloodForecastPopupFragment(Location location, Context context, GoogleMap map) {
        mForecastLocation = new Location(location.getLatitude(), location.getLongitude());
        iconGenerator = new IconGenerator(context);
        mMap = map;
    }

    public Location getForecastLocation() {
        return mForecastLocation;
    }

    // region IFloodForecastView...
    @Override
    public void renderForecastOnLocation(ForecastData forecastData) {
        styleFragment();
        LatLng latLng = new LatLng(mForecastLocation.getLatitude(), mForecastLocation.getLongitude());
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("Flood"))));
    }
    // endregion

    private void styleFragment() {
        iconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
    }
}