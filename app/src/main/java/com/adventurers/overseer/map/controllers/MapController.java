package com.adventurers.overseer.map.controllers;


import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.adventurers.overseer.Constants.EC_SERVER_ERROR;
import static com.adventurers.overseer.Constants.EC_SERVER_FAILED;
import static com.adventurers.overseer.Constants.EM_SERVER_ERROR;
import static com.adventurers.overseer.Constants.EM_SERVER_FAILED;

import androidx.annotation.NonNull;

import com.adventurers.overseer.api.FloodArea;
import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.map.interactors.MapInteractor;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.map.models.MapData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapController implements IMapController {
    private final MapInteractor mMapInteractor;

    public MapController(MapInteractor mapInteractor) {
        mMapInteractor = mapInteractor;
    }

    @Override
    public MapData getForecastsAroundLocation(Location location, double visibilityRadius) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<FloodArea>> call = overseerApi.getFloodAreas(location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<List<FloodArea>>() {
            @Override
            public void onResponse(@NonNull Call<List<FloodArea>> call, @NonNull Response<List<FloodArea>> response) {
                //
                if(!response.isSuccessful()){
                    onServerRequestFailed(EC_SERVER_ERROR, EM_SERVER_ERROR);
                    return;
                }
                if (response.body() != null) {
                    List<Location> forecasts = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    for(FloodArea floodArea : response.body()){
                        forecasts.add(new Location(floodArea.getLatitude(), floodArea.getLongitude()));
                        ids.add(floodArea.getId());
                    }
                    mMapInteractor.onSuccessRequest(new MapData(forecasts, ids));
                }
                else {
                    onServerRequestFailed(EC_SERVER_ERROR, EM_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FloodArea>> call, @NonNull Throwable t) {
                onServerRequestFailed(EC_SERVER_FAILED, EM_SERVER_FAILED);
            }
        });
        return null;
    }

    private void onServerRequestFailed(int code, String errorMessage){
        mMapInteractor.showRequestFailure(code, errorMessage);
    }
}