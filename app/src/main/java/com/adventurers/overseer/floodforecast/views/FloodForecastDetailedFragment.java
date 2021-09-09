package com.adventurers.overseer.floodforecast.views;

import static com.adventurers.overseer.Constants.TAG_FLOOD_FORECAST_DETAILED_FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.adventurers.overseer.R;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FloodForecastDetailedFragment extends BottomSheetDialogFragment implements IFloodForecastView {
    private Location mForecastLocation;

    public static FloodForecastDetailedFragment newInstance(ForecastData forecastData){
        return new FloodForecastDetailedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_floodforecast_detailed, container, false);
        return view;
    }

    // region BottomSheetDialogFragment...

    // endregion

    // region IFloodForecastView...
    @Override
    public void renderForecastOnLocation(ForecastData forecastData) {
        //this.show();
    }

    public void showDetailedFragment(FragmentManager fragmentManager) {
        this.show(fragmentManager, TAG_FLOOD_FORECAST_DETAILED_FRAGMENT);
    }

    // endregion

    private void styleFragment() {

    }
}
