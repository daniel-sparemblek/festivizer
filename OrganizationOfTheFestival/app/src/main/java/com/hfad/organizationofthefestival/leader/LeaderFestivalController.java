package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.FestivalClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderFestivalController {

    private LeaderFestivalActivity leaderFestivalActivity;
    private LeaderClient leaderApi;
    private FestivalClient festivalApi;
    private String accessToken;
    private String leaderID;
    private String refreshToken;

    public LeaderFestivalController(LeaderFestivalActivity leaderFestivalActivity, String accessToken, String leaderID, String refreshToken) {
        leaderApi = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        festivalApi = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FestivalClient.class);

        this.accessToken = accessToken;
        this.leaderID = leaderID;
        this.leaderFestivalActivity = leaderFestivalActivity;
        this.refreshToken = refreshToken;
    }
}
