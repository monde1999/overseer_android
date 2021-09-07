package com.adventurers.overseer.map.interactors;

import com.adventurers.overseer.map.controller.IMapController;
import com.adventurers.overseer.map.controller.MapController;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.models.MapData;
import com.adventurers.overseer.map.presenter.IMapPresenter;

public class MapInteractor {
    private final IMapPresenter mMapPresenter;
    private final IMapController mMapController;

    public MapInteractor(IMapPresenter iMapPresenter){
        mMapPresenter = iMapPresenter;
        mMapController = new MapController(this);
    }

    public void showForecastsAroundLocation(Location location, double visibilityRadius) {
        MapData mMapData = mMapController.getForecastsAroundLocation(location, visibilityRadius);
        mMapPresenter.presentForecastsAroundLocation(mMapData);
    }

    public void showRequestFailure(int errorCode, String errorMessage) {
        mMapPresenter.presentRequestFailure(errorCode, errorMessage);
    }
}