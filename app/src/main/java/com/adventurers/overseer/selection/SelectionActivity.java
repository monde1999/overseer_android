package com.adventurers.overseer.selection;



import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.adventurers.overseer.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class SelectionActivity {
    private final BottomSheetBehavior<View> mSelectionFragment;
    private final Button btn_directions;
    private OnExpandedHeightReady onExpandedHeightReady;

    public SelectionActivity(Activity activity, BottomSheetBehavior<View> selectionFragment) {
        mSelectionFragment = selectionFragment;
        hide();
        btn_directions = activity.findViewById(R.id.selection_btn_directions);
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

    public void setOnDirectionsClickListener(View.OnClickListener l) {
        btn_directions.setOnClickListener(l);
    }

    public void setOnExpandedHeightReady(OnExpandedHeightReady l) {
        onExpandedHeightReady = l;
    }

    public interface OnExpandedHeightReady {
        void onExpandedHeightReady(int height);
    }
}