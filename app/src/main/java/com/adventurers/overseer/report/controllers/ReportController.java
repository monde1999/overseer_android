package com.adventurers.overseer.report.controllers;

import static com.adventurers.overseer.Constants.BASE_URL_OVERSEER;

import com.adventurers.overseer.api.OverseerApi;
import com.adventurers.overseer.report.interactors.ReportInteractor;
import com.adventurers.overseer.report.models.CreateReportData;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportController implements IReportController {
    public static final String BASE_URL = BASE_URL_OVERSEER; // change to host ip of server accordingly
    Retrofit retrofit;
    ReportInteractor reportInteractor;
    public  ReportController(ReportInteractor reportInteractor){
        this.reportInteractor = reportInteractor;
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Override
    public void addReportToDb(CreateReportData reportData) {
        OverseerApi overseerApi = retrofit.create(OverseerApi.class);
        MultipartBody.Part[] imagesPart;
        if (reportData.getImages()!= null && !reportData.getImages().isEmpty()){
            imagesPart = new MultipartBody.Part[reportData.getImages().size()];
            for (int index = 0; index<reportData.getImages().size();index++) {
                File file = reportData.getImages().get(index);
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"),
                        file);
                imagesPart[index] = MultipartBody.Part.createFormData("ReportImages",
                        file.getName(),
                        imageBody);
            }
        }
        else{
            imagesPart = null;
        }
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),reportData.getDescription());
        Call<okhttp3.ResponseBody> call = overseerApi.createReport(reportData.getUser(), reportData.getDescription(),
                reportData.getLatitude(), reportData.getLongitude(), reportData.getFloodLevel(), imagesPart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                   reportInteractor.feedbackReportSuccess("Success");
                }
                else {
                    reportInteractor.feedbackReportProgressing("In progress");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                reportInteractor.feedbackReportError(001,"Unable to connect.");
            }
        });
    }
}