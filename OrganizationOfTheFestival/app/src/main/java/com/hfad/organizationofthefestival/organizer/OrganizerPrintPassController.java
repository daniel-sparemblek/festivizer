package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizerPrintPassController {
    private OrganizerPrintPassActivity organizerPrintPassActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;

    public OrganizerPrintPassController(OrganizerPrintPassActivity organizerPrintPassActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.organizerPrintPassActivity = organizerPrintPassActivity;
        this.refreshToken = refreshToken;
    }

    public void getOrganizer() {
        Call<Organizer> organizerCall = api.getData(username, "Bearer " + accessToken);

        organizerCall.enqueue(new Callback<Organizer>() {
            @Override
            public void onResponse(Call<Organizer> call, Response<Organizer> response) {
                if(response.isSuccessful()) {
                    organizerPrintPassActivity.fillInActivity(response.body(), response.body().getFestivalList());
                } else {
                    try {
                        Toast.makeText(organizerPrintPassActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(organizerPrintPassActivity, "Unable to get the error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Organizer> call, Throwable t) {
                Toast.makeText(organizerPrintPassActivity, "Fatal error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
