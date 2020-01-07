package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.utility.EventApply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class EventsController {

    private EventsActivity eventsActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private char c = 0x2714;

    public EventsController(EventsActivity eventsActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.eventsActivity = eventsActivity;
        this.refreshToken = refreshToken;
    }

    public void fetchEvents() {
        Call<EventApply[]> eventsCall = api.getAllEvents(username, "Bearer " + accessToken);

        eventsCall.enqueue(new Callback<EventApply[]>() {
            @Override
            public void onResponse(Call<EventApply[]> call, Response<EventApply[]> response) {

            }

            @Override
            public void onFailure(Call<EventApply[]> call, Throwable t) {

            }
        });
    }
}
