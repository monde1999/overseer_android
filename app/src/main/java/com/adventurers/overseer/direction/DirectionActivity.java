package com.adventurers.overseer.direction;



import android.app.Activity;
import android.graphics.drawable.Drawable;
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


public class DirectionActivity {
    private final BottomSheetBehavior<View> mSelectionFragment;
    private final Button btn_start;
    private final TextView tv_name;
    private final TextView tv_address;
    private final TextView tv_distance;
    private final ImageView iv_location;
    private OnExpandedHeightReady onExpandedHeightReady;

    public DirectionActivity(Activity activity, BottomSheetBehavior<View> selectionFragment) {
        mSelectionFragment = selectionFragment;
        hide();
        btn_start = activity.findViewById(R.id.direction_btn_start);
        tv_name = activity.findViewById(R.id.direction_tv_name);
        tv_address = activity.findViewById(R.id.direction_tv_address);
        tv_distance = activity.findViewById(R.id.direction_tv_distance);
        iv_location = activity.findViewById(R.id.direction_iv_location);
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

    public void setOnStartClickListener(View.OnClickListener l) {
        btn_start.setOnClickListener(l);
    }

    public void setOnExpandedHeightReady(OnExpandedHeightReady l) {
        onExpandedHeightReady = l;
    }

    public interface OnExpandedHeightReady {
        void onExpandedHeightReady(int height);
    }

    public void setName(String name) {
        tv_name.setText(name);
    }

    public void setAddress(String address) {
        tv_address.setText(address);
    }

    public void setDistance(String distance) {
        tv_distance.setText(distance);
    }

    public void setImage(Drawable drawable) {
        iv_location.setImageDrawable(drawable);
    }
}