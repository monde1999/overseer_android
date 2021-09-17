package com.adventurers.overseer.direction.presenters;

import com.adventurers.overseer.direction.interactors.DirectionInteractor;
import com.adventurers.overseer.direction.models.DirectionData;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.views.MapActivity;

public class DirectionPresenter implements IDirectionPresenter {
    private final MapActivity mMapActivity;

    public DirectionPresenter(MapActivity mapActivity) {
        mMapActivity = mapActivity;
    }

    @Override
    public void presentPath(DirectionData directionData) {
        mMapActivity.renderPaths(directionData.getRoutes());
    }

    @Override
    public void presentPathFindingUnsuccessful() {

    }

    public void present(Location currentLocation, Location goal) {
        DirectionInteractor interactor = new DirectionInteractor(this);
        interactor.showPath(currentLocation, goal);
    }
}