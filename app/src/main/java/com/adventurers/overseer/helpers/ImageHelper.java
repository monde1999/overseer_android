package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.BASE_URL_WEATHER;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adventurers.overseer.R;
import com.adventurers.overseer.map.models.Location;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ImageHelper {
    public static void loadWeatherIcon(String code, ImageView imageView, ProgressBar progressBar) {
        String url = "https://openweathermap.org/img/wn/" + code + "@4x.png";
        Picasso.get().load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void loadStreetStaticView(Location location, ImageView imageView, ProgressBar progressBar) {
        String size = "size=" + imageView.getWidth() + "x" + imageView.getHeight();
        String url = "https://maps.googleapis.com/maps/api/streetview?" + size + "&location=" + location.getLatitude() + "," + location.getLongitude() + "&key=" + imageView.getResources().getString(R.string.google_maps_key);
        Transformation transformation = new RoundedCornersTransformation(80,0);
        Picasso.get().load(url).transform(transformation).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
