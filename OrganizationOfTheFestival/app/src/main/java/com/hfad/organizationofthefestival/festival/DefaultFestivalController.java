package com.hfad.organizationofthefestival.festival;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultFestivalController {
    private DefaultFestivalActivity defaultFestivalActivity;
    private FestivalClient festivalApi;
    private String accessToken;
    private String festivalId;
    private String refreshToken;

    public DefaultFestivalController(DefaultFestivalActivity defaultFestivalActivity, String accessToken, String festivalId, String refreshToken) {
        festivalApi = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FestivalClient.class);

        this.accessToken = accessToken;
        this.festivalId = festivalId;
        this.defaultFestivalActivity = defaultFestivalActivity;
        this.refreshToken = refreshToken;
    }

    public void getFestivalData() {
        Call<Festival> festivalCall = festivalApi.getFestivalInfo(festivalId, "Bearer " + accessToken);

        festivalCall.enqueue(new Callback<Festival>() {
            @Override
            public void onResponse(Call<Festival> call, Response<Festival> response) {
                if(response.isSuccessful()) {
                    defaultFestivalActivity.fillInActivity(response.body());
                } else {
                    Toast.makeText(defaultFestivalActivity, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Festival> call, Throwable t) {
                System.out.println("Fatal error." + t.toString());
            }
        });
    }
}
