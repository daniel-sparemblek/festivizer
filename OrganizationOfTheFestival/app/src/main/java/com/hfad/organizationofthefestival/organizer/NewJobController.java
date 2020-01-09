package com.hfad.organizationofthefestival.organizer;

import android.app.Activity;
import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.EventApply;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NewJobController {

    private NewJobActivity newJobActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;

    public NewJobController(NewJobActivity newJobActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.newJobActivity = newJobActivity;
        this.refreshToken = refreshToken;
    }

    public void getEvent(int eventId) {
        Call<EventApply> eventCall = api.getEvent(String.valueOf(eventId), "Bearer " + accessToken);

        eventCall.enqueue(new Callback<EventApply>() {
            @Override
            public void onResponse(Call<EventApply> call, Response<EventApply> response) {
                if (response.isSuccessful()) {
                    newJobActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(newJobActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(newJobActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventApply> call, Throwable t) {
                Toast.makeText(newJobActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
