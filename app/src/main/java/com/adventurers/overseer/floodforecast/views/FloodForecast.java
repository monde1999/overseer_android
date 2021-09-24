package com.adventurers.overseer.floodforecast.views;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class FloodForecast {
    private final List<FloodForecastPopupFragment> popupFragments;

    public FloodForecast(List<Location> locations, Context context, GoogleMap map) {
        popupFragments = new ArrayList<>();
        for (Location location : locations) {
            popupFragments.add(new FloodForecastPopupFragment(location, context, map));
        }
    }

    public List<FloodForecastPopupFragment> getPopupFragments() {
        return popupFragments;
    }

    public static void showDetailedFragment(List<FloodForecastPopupFragment> popupFragments, Marker marker, FragmentManager fragmentManager) {
        for (FloodForecastPopupFragment popupFragment : popupFragments) {
            if(marker.equals(popupFragment.getMarker())) {
                popupFragment.showDetailedFragment(fragmentManager);
                break;
            }
        }
    }
}