package com.adventurers.overseer.map.presenter;

import com.adventurers.overseer.map.models.MapData;

public interface IMapPresenter {
    void presentForecastsAroundLocation(MapData mapData);
    void presentRequestFailure(int errorCode, String errorMessage);
}