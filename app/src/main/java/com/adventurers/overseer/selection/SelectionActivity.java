package com.adventurers.overseer.selection;



import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adventurers.overseer.R;
import com.adventurers.overseer.helpers.ImageHelper;
import com.adventurers.overseer.map.models.Location;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;


public class SelectionActivity {
    private final BottomSheetBehavior<View> mSelectionFragment;
    private final Button btn_directions;
    private final Button btn_start;
    private final TextView tv_name;
    private final TextView tv_address;
    private final TextView tv_distance;
    private final ImageView iv_location;
    private OnExpandedHeightReady onExpandedHeightReady;

    public SelectionActivity(Activity activity, BottomSheetBehavior<View> selectionFragment) {
        mSelectionFragment = selectionFragment;
        hide();
        btn_directions = activity.findViewById(R.id.selection_btn_directions);
        btn_start = activity.findViewById(R.id.selection_btn_start);
        tv_name = activity.findViewById(R.id.selection_tv_name);
        tv_address = activity.findViewById(R.id.selection_tv_address);
        tv_distance = activity.findViewById(R.id.selection_tv_distance);
        iv_location = activity.findViewById(R.id.selection_iv_location);
        mSelectionFragment.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_EXPANDED) {
                    onExpandedHeightReady.onExpandedHeightReady(bottomSheet.getHeight());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void show() {
        mSelectionFragment.setState(BottomSheetBehavior.STATE_EXPANDED);
        mSelectionFragment.setHideable(false);
    }

    public void hide() {
        mSelectionFragment.setHideable(true);
        mSelectionFragment.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void setContents(String name, String address, Location origin, Location destination) {
        tv_name.setText(name);
        tv_address.setText(address);
        DecimalFormat df=new DecimalFormat("#.## km");
        double distance = getDistanceKm(origin, destination);
        tv_distance.setText(df.format(distance));
        ImageHelper.loadStreetStaticView(destination, iv_location);
    }

    public void setOnDirectionsClickListener(View.OnClickListener l) {
        btn_directions.setOnClickListener(l);
    }

    public void setOnExpandedHeightReady(OnExpandedHeightReady l) {
        onExpandedHeightReady = l;
    }

    public interface OnExpandedHeightReady {
        void onExpandedHeightReady(int height);
    }

    private double getDistanceKm(Location origin, Location destination) {
        float[] results = new float[5];
        android.location.Location.distanceBetween(
                origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude(),
                results
        );
        return results[0] / 1000;
    }
}