package com.adventurers.overseer.map.interactors;

import com.adventurers.overseer.map.controllers.IMapController;
import com.adventurers.overseer.map.controllers.MapController;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.models.MapData;
import com.adventurers.overseer.map.presenters.IMapPresenter;

public class MapInteractor {
    private final IMapPresenter mMapPresenter;

    public MapInteractor(IMapPresenter iMapPresenter){
        mMapPresenter = iMapPresenter;
    }

    public void showForecastsAroundLocation(Location location, double visibilityRadius) {
        IMapController mMapController = new MapController(this);
        mMapController.getForecastsAroundLocation(location, visibilityRadius);
    }

    public void showRequestFailure(int errorCode, String errorMessage) {
        mMapPresenter.presentRequestFailure(errorCode, errorMessage);
    }

    public void onSuccessRequest(MapData mapData) {
        mMapPresenter.presentForecastsAroundLocation(mapData);
    }
}