package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.EventApply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsController {

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
        System.out.println("Usao sam u fetchEvents");
        Call<EventApply[]> eventsCall = api.getAllEvents(username, "Bearer " + accessToken);

        eventsCall.enqueue(new Callback<EventApply[]>() {
            @Override
            public void onResponse(Call<EventApply[]> call, Response<EventApply[]> response) {
                if(response.isSuccessful()) {
                    if(response.body() == null) {
                        System.out.println("AAAAAAAAAAAAA");
                        return;
                    }
                    eventsActivity.fillInActivity(response.body());
                } else {
                    try {
                        Toast.makeText(eventsActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(eventsActivity, "Unable to display error message 8=D", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventApply[]> call, Throwable t) {
                Toast.makeText(eventsActivity, "Something went terribly wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void fetchCompletedEvents() {
//        System.out.println("Usao sam u fetchCompletedEvents");
//        Call<EventApply[]> eventsCall = api.getAllEvents(username, "Bearer " + accessToken);
//
//        eventsCall.enqueue(new Callback<EventApply[]>() {
//            @Override
//            public void onResponse(Call<EventApply[]> call, Response<EventApply[]> response) {
//                if(response.isSuccessful()) {
//                    if(response.body() == null) {
//                        System.out.println("AAAAAAAAAAAAA");
//                        return;
//                    }
//                    eventsActivity.fillInCompletedActivity(response.body());
//                } else {
//                    try {
//                        Toast.makeText(eventsActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        Toast.makeText(eventsActivity, "Unable to display error message 8=D", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EventApply[]> call, Throwable t) {
//                Toast.makeText(eventsActivity, "Something went terribly wrong :(", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public List<String> format(EventApply[] events) {
        List<String> result = new ArrayList<>();

        for(EventApply event : events) {
            result.add(event.getName());
            System.out.println("POZIV");
            System.out.println(event.getName());
        }

        return result;
    }
}
