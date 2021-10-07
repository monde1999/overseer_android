package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.api.ReactionsCount;
import com.adventurers.overseer.api.ReportData;
import com.adventurers.overseer.api.ReportImage;
import com.adventurers.overseer.api.ReportReactData;
import com.adventurers.overseer.api.ReportReactResponse;
import com.adventurers.overseer.api.ReportReaction;
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

    public static void fetchReactionsCountForReport(int id, TextView tv_likes, TextView tv_dislikes) {
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
                    String likes = response.body().getPositive()+"";
                    String dislikes = response.body().getNegative()+"";
                    tv_likes.setText(likes);
                    tv_dislikes.setText(dislikes);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReactionsCount> call, @NonNull Throwable t) {

            }
        });
    }

    public static void fetchReactionForReport(int reportId, int userId, ReportsAdapter.ReportsViewHolder holder) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<ReportReaction>> call = overseerApi.getReportReaction(reportId, userId);
        call.enqueue(new Callback<List<ReportReaction>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportReaction>> call, @NonNull Response<List<ReportReaction>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null && response.body().size() > 0) {
                    holder.toggleLikeDislike(response.body().get(0).isPositive());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportReaction>> call, @NonNull Throwable t) {

            }
        });
    }

    public static void postReportReaction(ReportReactData reportReactData) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<ReportReactResponse> call = overseerApi.postReportReaction(reportReactData);
        call.enqueue(new Callback<ReportReactResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportReactResponse> call, @NonNull Response<ReportReactResponse> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportReactResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
