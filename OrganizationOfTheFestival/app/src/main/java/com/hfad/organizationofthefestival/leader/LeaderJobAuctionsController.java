package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderJobAuctionsController {
    private LeaderJobAuctionsActivity activity;
    private String accessToken;
    private String refreshToken;
    private String leaderID;
    private LeaderClient api;

    public LeaderJobAuctionsController(LeaderJobAuctionsActivity activity, String accessToken, String refreshToken, String leaderID) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.activity = activity;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.leaderID = leaderID;
    }

    public void getJobAuctions() {
        System.out.println("kurva:" +leaderID);
        Call<ApplicationResponse[]> call = api.getAuctions(leaderID, "Bearer " + accessToken);

        call.enqueue(new Callback<ApplicationResponse[]>() {
            @Override
            public void onResponse(Call<ApplicationResponse[]> call, Response<ApplicationResponse[]> response) {
                if (response.isSuccessful()) {
                    activity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<ApplicationResponse[]> call, Throwable t) {

            }
        });
    }
}
