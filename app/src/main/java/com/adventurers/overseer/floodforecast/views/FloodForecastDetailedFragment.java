package com.adventurers.overseer.floodforecast.views;

import static com.adventurers.overseer.Constants.TAG_FLOOD_FORECAST_DETAILED_FRAGMENT;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.adventurers.overseer.R;
import com.adventurers.overseer.floodforecast.models.ForecastData;
import com.adventurers.overseer.floodforecast.presenters.FloodForecastPresenter;
import com.adventurers.overseer.helpers.StringHelper;
import com.adventurers.overseer.map.models.Location;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

public class FloodForecastDetailedFragment extends BottomSheetDialogFragment implements IFloodForecastView {
    private Location mForecastLocation;
    private View view;

    public static FloodForecastDetailedFragment newInstance(Location location) {
        FloodForecastDetailedFragment fragment = new FloodForecastDetailedFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        args.putString("Location", json);
        fragment.setArguments(args);
        return fragment;
    }

    // region DialogFragment...
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Called before showing the dialog
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            String json = getArguments().getString("Location");
            Gson gson = new Gson();
            mForecastLocation = gson.fromJson(json, Location.class);
            FloodForecastPresenter presenter = new FloodForecastPresenter(this);
            presenter.present(mForecastLocation);
        }
    }
    // endregion

    // region BottomSheetDialogFragment...
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                if(bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                view = getView();
                // Set details
            }
        });
        return dialog;
    }
    // endregion

    // region Fragment...
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floodforecast_detailed, container, false);
    }
    // endregion

    // region IFloodForecastView...
    @Override
    public void renderForecastOnLocation(ForecastData forecastData) {
        if(view != null) {
            TextView tv_warning = view.findViewById(R.id.forecast_tv_warning);
            ProgressBar pb_street = view.findViewById(R.id.forecast_pb_street);
            ImageView iv_street = view.findViewById(R.id.forecast_iv_street);
            TextView tv_location = view.findViewById(R.id.forecast_tv_location);
            TextView tv_temp = view.findViewById(R.id.forecast_tv_temp);
            TextView tv_rain = view.findViewById(R.id.forecast_tv_rain);
            ProgressBar pb_weather = view.findViewById(R.id.forecast_pb_weather);
            ImageView iv_weather = view.findViewById(R.id.forecast_iv_weather);
            TextView tv_weather_status = view.findViewById(R.id.forecast_tv_weather_status);
            TextView tv_morning = view.findViewById(R.id.forecast_tv_morning);
            TextView tv_afternoon = view.findViewById(R.id.forecast_tv_afternoon);
            TextView tv_evening = view.findViewById(R.id.forecast_tv_evening);
            TextView tv_night = view.findViewById(R.id.forecast_tv_night);

            tv_location.setText(forecastData.getLocation().toString());
            String temp = forecastData.getCurrent_temp()+"°";
            tv_temp.setText(temp);
            String rain = forecastData.getRain()+"mm ("+forecastData.getClouds()+"%)";
            tv_rain.setText(rain);
            tv_weather_status.setText(StringHelper.capitalizeWord(forecastData.getWeather_status()));
            String tempMorn = forecastData.getMorn_temp()+"°";
            tv_morning.setText(tempMorn);
            String tempAft = forecastData.getAft_temp()+"°";
            tv_afternoon.setText(tempAft);
            String tempEve = forecastData.getEve_temp()+"°";
            tv_evening.setText(tempEve);
            String tempNight = forecastData.getNight_temp()+"°";
            tv_night.setText(tempNight);
        }
    }

    public void showDetailedFragment(FragmentManager fragmentManager) {
        this.show(fragmentManager, TAG_FLOOD_FORECAST_DETAILED_FRAGMENT);
    }
    // endregion

    private void styleFragment() {

    }
}