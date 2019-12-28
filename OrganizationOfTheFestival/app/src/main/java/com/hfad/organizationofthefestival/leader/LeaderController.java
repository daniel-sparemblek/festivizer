package com.hfad.organizationofthefestival.leader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderController {

    private LeaderActivity leaderActivity;
    private Retrofit.Builder builder;

    public LeaderController(LeaderActivity leaderActivity) {
        builder = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create());

        this.leaderActivity = leaderActivity;
    }
}
