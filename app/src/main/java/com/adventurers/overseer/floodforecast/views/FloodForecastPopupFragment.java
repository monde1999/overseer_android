package com.adventurers.overseer.floodforecast.views;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

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
    private final IconGenerator mIconGenerator;
    private Marker mMarker;
    private final GoogleMap mMap;
    private final FloodForecastDetailedFragment mFloodForecastDetailedFragment;

    public FloodForecastPopupFragment(Location location, Context context, GoogleMap map) {
        mForecastLocation = new Location(location.getLatitude(), location.getLongitude());
        mIconGenerator = new IconGenerator(context);
        mMap = map;
        mFloodForecastDetailedFragment = FloodForecastDetailedFragment.newInstance(location);
    }

    public Location getForecastLocation() {
        return mForecastLocation;
    }

    // region IFloodForecastView...
    @Override
    public void renderForecastOnLocation(ForecastData forecastData) {
        styleFragment();
        LatLng latLng = new LatLng(mForecastLocation.getLatitude(), mForecastLocation.getLongitude());
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon("Flood"))));
    }
    // endregion

    private void styleFragment() {
        mIconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void showDetailedFragment(FragmentManager fragmentManager) {
        mFloodForecastDetailedFragment.showDetailedFragment(fragmentManager);
    }

    public void remove() {
        mMarker.remove();
    }
}