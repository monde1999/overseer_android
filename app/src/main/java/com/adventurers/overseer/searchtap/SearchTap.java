package com.adventurers.overseer.searchtap;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;

public class SearchTap {
    private OnPlaceDetailsRequestListener onPlaceDetailsRequestListener;
    private final Context context;

    public SearchTap(Context context) {
        this.context = context;
    }

    public void requestPlaceDetails(PointOfInterest pointOfInterest) {
        final String placeId = pointOfInterest.placeId;
        final List<Place.Field> placeFields = Collections.singletonList(Place.Field.ADDRESS);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        PlacesClient placesClient = Places.createClient(context);
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place p = fetchPlaceResponse.getPlace();
                Place place = Place.builder()
                        .setName(pointOfInterest.name)
                        .setLatLng(pointOfInterest.latLng)
                        .setId(pointOfInterest.placeId)
                        .setAddress(p.getAddress())
                        .build();
                onPlaceDetailsRequestListener.onSuccess(place);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnPlaceDetailsRequestListener(OnPlaceDetailsRequestListener l) {
        onPlaceDetailsRequestListener = l;
    }

    public interface OnPlaceDetailsRequestListener {
        void onSuccess(Place place);
    }
}