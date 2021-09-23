package com.adventurers.overseer.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.TextView;

import com.adventurers.overseer.map.models.Location;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class GeocoderHelper {
    public static void getLocationAddress(Location location, TextView textView){
        Geocoder geocoder = new Geocoder(textView.getContext());
        String addressName = textView.getText().toString();
        List<Address> address;
        try {
            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            addressName = address.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText(addressName);
    }
}