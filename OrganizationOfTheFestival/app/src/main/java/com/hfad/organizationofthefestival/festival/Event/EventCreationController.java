package com.hfad.organizationofthefestival.festival.Event;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.FestivalClient;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventCreationController {

    private CreateEventActivity activity;
    private CreateEventApi api;
    private FestivalClient festApi;
    private String accessToken;

    private String festivalId;
    private String refreshToken;

    public EventCreationController(CreateEventActivity activity, String accessToken, String festivalId, String refreshToken) {
        this.accessToken = accessToken;
        this.festivalId = festivalId;
        this.refreshToken = refreshToken;

        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CreateEventApi.class);
        this.activity = activity;

        festApi = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FestivalClient.class);
    }

    public void createEvent(WorkingEvent event, String accessToken) {
        Call<CreateEventResponse> call =
                api.createEvent("Bearer " + accessToken, event);

        call.enqueue(new Callback<CreateEventResponse>() {
            @Override
            public void onResponse(Call<CreateEventResponse> call, Response<CreateEventResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Successfully created event!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(activity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(activity, "Unable to parse the error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateEventResponse> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOrganizers() {
        Call<Organizer[]> orgCall = festApi.getOrganizers(festivalId, "Bearer " + accessToken);

        orgCall.enqueue(new Callback<Organizer[]>() {
            @Override
            public void onResponse(Call<Organizer[]> call, Response<Organizer[]> response) {
                if (response.isSuccessful()) {
                    activity.setupSpinner(response.body());
                } else {
                    try {
                        System.out.println("KAKAKAKAKA" + response.errorBody().string());
                    } catch (IOException e) {
                        System.out.println("NONONO NO ERROR PARSERINJOS AMIGOS");
                    }
                }
            }

            @Override
            public void onFailure(Call<Organizer[]> call, Throwable t) {
                System.out.println("ZELIM SMRT " + t.getMessage() + " " + festivalId);
            }
        });
    }
}
