package com.hfad.organizationofthefestival.leader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderController {

    private LeaderActivity leaderActivity;
    private Retrofit retrofit;
    private LeaderClient api;

    public LeaderController(LeaderActivity leaderActivity) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(LeaderClient.class);
        this.leaderActivity = leaderActivity;
    }
}
