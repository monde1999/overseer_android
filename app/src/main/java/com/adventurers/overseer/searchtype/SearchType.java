package com.adventurers.overseer.searchtype;

import static com.adventurers.overseer.Constants.MAPS_API_KEY;
import static com.adventurers.overseer.Constants.RC_SEARCH_TYPE;

import android.app.Activity;
import android.content.Intent;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class SearchType {
    public static void launch(Activity activity) {
        Places.initialize(activity, MAPS_API_KEY);
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                fieldList).build(activity);
        activity.startActivityForResult(intent, RC_SEARCH_TYPE);
    }
}