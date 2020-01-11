package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.OnAuctionResponse;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobAuctionController {

    private JobAuctionActivity jobAuctionActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private int jobId;

    public JobAuctionController(JobAuctionActivity jobAuctionActivity, String accessToken, int jobId, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.jobId = jobId;
        this.jobAuctionActivity = jobAuctionActivity;
        this.refreshToken = refreshToken;
    }

    public void getJobInfo() {
        Call<JobApply> jobApplyCall = api.getJobInfo(jobId, "Bearer " + accessToken);

        jobApplyCall.enqueue(new Callback<JobApply>() {
            @Override
            public void onResponse(Call<JobApply> call, Response<JobApply> response) {
                if (response.isSuccessful()) {
                    jobAuctionActivity.fillInActivity(response.body());
                } else {
                    try {
                        Toast.makeText(jobAuctionActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(jobAuctionActivity, "Unable to parse the error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JobApply> call, Throwable t) {
                Toast.makeText(jobAuctionActivity, "Fatal error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAuction(int jobId) {
        HashMap<String, String> body = new HashMap<>();
        body.put("job_id", String.valueOf(jobId));
        Call<Void> jobApplyCall = api.createAuction(body, "Bearer " + accessToken);

        jobApplyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(jobAuctionActivity, "Success!", Toast.LENGTH_SHORT).show();
                    jobAuctionActivity.blockButton();
                } else {
                    try {
                        Toast.makeText(jobAuctionActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(jobAuctionActivity, "Unable to parse the error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(jobAuctionActivity, "Fatal error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hasAuction(int jobId) {
        HashMap<String, String> body = new HashMap<>();
        body.put("job_id", String.valueOf(jobId));
        Call<OnAuctionResponse> jobApplyCall = api.checkAuction(body, "Bearer " + accessToken);

        jobApplyCall.enqueue(new Callback<OnAuctionResponse>() {
            @Override
            public void onResponse(Call<OnAuctionResponse> call, Response<OnAuctionResponse> response) {
                if (response.isSuccessful()) {
                    jobAuctionActivity.setButton(response.body());
                } else {
                    try {
                        Toast.makeText(jobAuctionActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(jobAuctionActivity, "Unable to parse the error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OnAuctionResponse> call, Throwable t) {
                Toast.makeText(jobAuctionActivity, "Fatal error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
