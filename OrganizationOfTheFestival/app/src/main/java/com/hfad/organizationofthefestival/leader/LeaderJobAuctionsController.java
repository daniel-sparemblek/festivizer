package com.hfad.organizationofthefestival.leader;

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
        Call<Application[]> call = api.getAuctions(leaderID, "Bearer " + accessToken);

        call.enqueue(new Callback<Application[]>() {
            @Override
            public void onResponse(Call<Application[]> call, Response<Application[]> response) {
                if (response.isSuccessful()) {
                    List<Application> applications = Arrays.asList(response.body());
                    activity.fillInActivity(applications);
                }
            }

            @Override
            public void onFailure(Call<Application[]> call, Throwable t) {

            }
        });
    }
}
