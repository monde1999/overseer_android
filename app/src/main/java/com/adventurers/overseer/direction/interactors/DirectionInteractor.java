package com.adventurers.overseer.direction.interactors;

import com.adventurers.overseer.direction.controllers.DirectionController;
import com.adventurers.overseer.direction.models.DirectionData;
import com.adventurers.overseer.direction.presenters.IDirectionPresenter;
import com.adventurers.overseer.map.models.Location;

public class DirectionInteractor {
    private final IDirectionPresenter mDirectionPresenter;

    public DirectionInteractor(IDirectionPresenter directionPresenter) {
        mDirectionPresenter = directionPresenter;
    }

    public void showPath(Location currentLocation, Location goal) {
        DirectionController controller = new DirectionController(this);
        controller.findPath(currentLocation, goal);
    }
    public void showRequestFailure(int errorCode, String errorMessage) {
        mDirectionPresenter.presentPathFindingUnsuccessful();
    }

    public void onSuccessRequest(DirectionData directionData) {
        mDirectionPresenter.presentPath(directionData);
    }
}