package com.adventurers.overseer.direction.presenters;

import com.adventurers.overseer.direction.models.DirectionData;
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
}