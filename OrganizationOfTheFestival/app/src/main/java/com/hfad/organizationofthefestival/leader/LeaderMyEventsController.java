package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.event.Event;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class LeaderMyEventsController {

    private LeaderMyEventsActivity leaderMyEventsActivity;
    private String accessToken;
    private String refreshToken;
    private LeaderClient api;

    public LeaderMyEventsController(LeaderMyEventsActivity leaderMyEventsActivity, String accessToken, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.leaderMyEventsActivity = leaderMyEventsActivity;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void getActiveEvents(String festivalId) {
        Call<Event[]> call = api.getActiveEvents(festivalId, "Bearer " + accessToken);

        call.enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if (response.isSuccessful()) {
                    leaderMyEventsActivity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {

            }
        });
    }

    public void getCompletedEvents(String festivalId) {
        Call<Event[]> call = api.getCompletedEvents(festivalId, "Bearer " + accessToken);

        call.enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if (response.isSuccessful()) {
                    leaderMyEventsActivity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {

            }
        });
    }

    public void getPendingEvents(String festivalId) {
        Call<Event[]> call = api.getPendingEvents(festivalId, "Bearer " + accessToken);

        call.enqueue(new Callback<Event[]>() {
            @Override
            public void onResponse(Call<Event[]> call, Response<Event[]> response) {
                if (response.isSuccessful()) {
                    leaderMyEventsActivity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<Event[]> call, Throwable t) {

            }
        });
    }
}
