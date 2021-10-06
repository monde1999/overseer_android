package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.api.ReactionsCount;
import com.adventurers.overseer.api.ReportData;
import com.adventurers.overseer.api.ReportImage;
import com.adventurers.overseer.map.models.Location;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportsHelper {
    public static void fetchReportsOnLocation(Location location, RecyclerView recyclerView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<ReportData>> call = overseerApi.getReports(location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<List<ReportData>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportData>> call, @NonNull Response<List<ReportData>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    ReportsAdapter adapter = new ReportsAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportData>> call, @NonNull Throwable t) {

            }
        });
    }

    public static void fetchImagesForReport(int id, RecyclerView recyclerView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<ReportImage>> call = overseerApi.getImages(id);
        call.enqueue(new Callback<List<ReportImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportImage>> call, @NonNull Response<List<ReportImage>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    ReportsImagesAdapter adapter = new ReportsImagesAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportImage>> call, @NonNull Throwable t) {

            }
        });
    }

    public static void fetchReactionsCountForReport(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<ReactionsCount> call = overseerApi.getReactionsCount(id);
        call.enqueue(new Callback<ReactionsCount>() {
            @Override
            public void onResponse(@NonNull Call<ReactionsCount> call, @NonNull Response<ReactionsCount> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReactionsCount> call, @NonNull Throwable t) {

            }
        });
    }
}
