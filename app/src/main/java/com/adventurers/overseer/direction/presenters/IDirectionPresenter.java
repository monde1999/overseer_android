package com.adventurers.overseer.direction.presenters;

import com.adventurers.overseer.direction.models.DirectionData;

public interface IDirectionPresenter {
    void presentPath(DirectionData directionData);
    void presentPathFindingUnsuccessful();
}
