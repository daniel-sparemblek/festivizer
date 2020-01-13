package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.FestivalClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderFestivalController {

    private LeaderFestivalActivity leaderFestivalActivity;
    private LeaderClient leaderApi;
    private FestivalClient festivalApi;
    private String accessToken;
    private String festivalId;
    private String refreshToken;

    public LeaderFestivalController(LeaderFestivalActivity leaderFestivalActivity, String accessToken, String festivalId, String refreshToken) {
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
        this.festivalId = festivalId;
        this.leaderFestivalActivity = leaderFestivalActivity;
        this.refreshToken = refreshToken;
    }

    public void getFestivalData() {
        Call<Festival> festivalCall = festivalApi.getFestivalInfo(festivalId, "Bearer " + accessToken);

        festivalCall.enqueue(new Callback<Festival>() {
            @Override
            public void onResponse(Call<Festival> call, Response<Festival> response) {
                if(response.isSuccessful()) {
                    leaderFestivalActivity.fillInActivity(response.body());
                } else {
                    Toast.makeText(leaderFestivalActivity, "KAWABOONGAAA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Festival> call, Throwable t) {
                Toast.makeText(leaderFestivalActivity, "AJMO KRAGUJEVAC", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
