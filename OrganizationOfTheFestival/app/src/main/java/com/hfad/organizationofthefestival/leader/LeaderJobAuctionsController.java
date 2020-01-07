package com.hfad.organizationofthefestival.leader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderJobAuctionsController {
    private LeaderJobAuctionsActivity activity;
    private String accessToken;
    private String refreshToken;
    private LeaderClient api;

    public LeaderJobAuctionsController(LeaderJobAuctionsActivity activity, String accessToken, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.activity = activity;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void getJobAuctions(){

    }
}
