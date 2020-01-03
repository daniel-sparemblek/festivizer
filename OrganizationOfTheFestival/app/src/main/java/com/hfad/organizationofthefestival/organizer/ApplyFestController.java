package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.festival.Festival;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplyFestController {
    private ApplyFestActivity applyFestActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public ApplyFestController(ApplyFestActivity applyFestActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.applyFestActivity = applyFestActivity;
        this.refreshToken = refreshToken;
    }

    public void fetchFestivals() {
        Call<Festival[]> festivalsCall = api.getFestivals("Bearer " + accessToken);

        festivalsCall.enqueue(new Callback<Festival[]>() {
            @Override
            public void onResponse(Call<Festival[]> call, Response<Festival[]> response) {
                if (response.isSuccessful()) {
                    applyFestActivity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<Festival[]> call, Throwable t) {

            }
        });
    }
}
