package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizerController {
    private OrganizerActivity organizerActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;

    public OrganizerController(OrganizerActivity organizerActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.organizerActivity = organizerActivity;
        this.refreshToken = refreshToken;
    }

    public void getOrganizer() {
        Call<Organizer> leaderCall = api.getData(username, "Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Organizer>() {
            @Override
            public void onResponse(Call<Organizer> call, Response<Organizer> response) {
                if(response.isSuccessful()) {
                    organizer = response.body();
                    organizerActivity.fillInActivity(organizer);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Organizer> call, Throwable t) {
                Toast.makeText(organizerActivity, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
