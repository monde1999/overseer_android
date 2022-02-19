package com.adventurers.overseer.helpers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;
import static com.adventurers.overseer.Constants.preferencesKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.api.ReportData;
import com.adventurers.overseer.api.ReportImage;
import com.adventurers.overseer.api.ReportReactResponse;
import com.adventurers.overseer.api.ReportReactionCount;
import com.adventurers.overseer.api.ReportReactionData;
import com.adventurers.overseer.api.ReportUserReaction;
import com.adventurers.overseer.floodforecast.views.ReportsAdapter;
import com.adventurers.overseer.floodforecast.views.ReportsImagesAdapter;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.user.handlers.UserInfoHandler;

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

    public static void fetchReportsOnLocation(Integer id, RecyclerView recyclerView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        SharedPreferences sharedPreferences = recyclerView.getContext().getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        Call<List<ReportData>> call = overseerApi.getReports("Token " + UserInfoHandler.getCurrentAccountToken(sharedPreferences), id);
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
        Call<List<ReportImage>> call = overseerApi.getReportImages(id);
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
        Call<ReportReactionCount> call = overseerApi.getReportReactionCount(id);
        call.enqueue(new Callback<ReportReactionCount>() {
            @Override
            public void onResponse(@NonNull Call<ReportReactionCount> call, @NonNull Response<ReportReactionCount> response) {
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
            public void onFailure(@NonNull Call<ReportReactionCount> call, @NonNull Throwable t) {

            }
        });
    }

    public static void fetchReactionForReport(int reportId, int userId, ReportsAdapter.ReportsViewHolder holder) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        Call<List<ReportUserReaction>> call = overseerApi.getReportUserReaction(reportId, userId);
        call.enqueue(new Callback<List<ReportUserReaction>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportUserReaction>> call, @NonNull Response<List<ReportUserReaction>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null && response.body().size() > 0) {
                    holder.toggleLikeDislike(response.body().get(0).isPositive());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportUserReaction>> call, @NonNull Throwable t) {

            }
        });
    }

    public static void postReportReaction(ReportReactionData reportReactionData, TextView tv_likes, TextView tv_dislikes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_OVERSEER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        SharedPreferences sharedPreferences = tv_likes.getContext().getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        Call<ReportReactResponse> call = overseerApi.postReportReaction("Token " + UserInfoHandler.getCurrentAccountToken(sharedPreferences), reportReactionData);
        call.enqueue(new Callback<ReportReactResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportReactResponse> call, @NonNull Response<ReportReactResponse> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    // Update report reactions count
                    ReportsHelper.fetchReactionsCountForReport(reportReactionData.getReport(), tv_likes, tv_dislikes);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportReactResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
