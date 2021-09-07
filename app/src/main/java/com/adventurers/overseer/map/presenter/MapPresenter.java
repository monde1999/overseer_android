package com.adventurers.overseer.map.presenter;

import com.adventurers.overseer.floodforecast.views.FloodForecastPopupFragment;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.interactors.MapInteractor;
import com.adventurers.overseer.map.models.MapData;
import com.adventurers.overseer.map.views.IMapView;

import java.util.ArrayList;
import java.util.List;

public class MapPresenter implements IMapPresenter {
    private final IMapView mMapView;

    public MapPresenter(IMapView iMapView) {
        mMapView = iMapView;
    }

    @Override
    public void presentForecastsAroundLocation(MapData mapData) {
        List<FloodForecastPopupFragment> forecasts = new ArrayList<>();
        for(Location location : mapData.getForecasts()){
            forecasts.add(new FloodForecastPopupFragment(location.getLatitude(), location.getLongitude()));
        }
        mMapView.renderForecasts(forecasts);
    }

    @Override
    public void presentRequestFailure(int errorCode, String errorMessage) {
        mMapView.renderError(errorCode, errorMessage);
    }

    public void present(Location location, double visibilityRadius) {
        MapInteractor mapInteractor = new MapInteractor(this);
        mapInteractor.showForecastsAroundLocation(location, visibilityRadius);
    }
}