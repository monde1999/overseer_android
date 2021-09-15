package com.adventurers.overseer.direction.presenters;

import static com.adventurers.overseer.Constants.TAG_DIRECTION_MODULE;
import static com.adventurers.overseer.Constants.TAG_MAP_MODULE;

import android.util.Log;

import com.adventurers.overseer.direction.interactors.DirectionInteractor;
import com.adventurers.overseer.direction.models.DirectionData;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.views.IMapView;
import com.adventurers.overseer.map.views.MapActivity;

public class DirectionPresenter implements IDirectionPresenter {
    private final MapActivity mMapActivity;

    public DirectionPresenter(MapActivity mapActivity) {
        mMapActivity = mapActivity;
    }

    @Override
    public void presentPath(DirectionData directionData) {
        mMapActivity.renderPath(directionData.getPath());
    }

    @Override
    public void presentPathFindingUnsuccessful() {

    }

    public void present(Location currentLocation, Location goal) {
        Log.d(TAG_DIRECTION_MODULE, "Direction presenter");
        DirectionInteractor interactor = new DirectionInteractor(this);
        interactor.showPath(currentLocation, goal);
    }
}