package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.BASE_URL_WEATHER;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageHelper {
    public static void loadWeatherIcon(Context context, String code, ImageView imageView, ProgressBar progressBar) {
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
}
