package com.adventurers.overseer.floodforecast.views;

import static com.adventurers.overseer.Constants.TAG_FLOOD_FORECAST_DETAILED_FRAGMENT;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.adventurers.overseer.R;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.map.models.Location;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FloodForecastDetailedFragment extends BottomSheetDialogFragment implements IFloodForecastView {
    private Location mForecastLocation;

    public static FloodForecastDetailedFragment newInstance(ForecastData forecastData){
        return new FloodForecastDetailedFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
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
