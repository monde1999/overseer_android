package com.adventurers.overseer.map.presenters;

import android.content.Context;

import com.adventurers.overseer.floodforecast.views.FloodForecast;
import com.adventurers.overseer.map.interactors.MapInteractor;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.models.MapData;
import com.adventurers.overseer.map.views.IMapView;
import com.adventurers.overseer.map.views.MapActivity;

public class MapPresenter implements IMapPresenter {
    private final IMapView mMapView;
    private Context context;

    public MapPresenter(IMapView iMapView) {
        mMapView = iMapView;
    }

    @Override
    public void presentForecastsAroundLocation(MapData mapData) {
//        List<FloodForecastPopupFragment> forecasts = new ArrayList<>();
//        for(Location location : mapData.getForecasts()){
//            forecasts.add(new FloodForecastPopupFragment(location));
//        }
//        mMapView.renderForecasts(forecasts);
        FloodForecast floodForecast = new FloodForecast(mapData.getForecasts(),(Context)mMapView, ((MapActivity)mMapView).getMap());
        mMapView.renderForecasts(floodForecast.getPopupFragments());
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