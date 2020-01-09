package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.event.Event;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ViewEventController {

    private ViewEventActivity viewEventActivity;
    private LeaderClient api;
    private String accessToken;
    private String refreshToken;

    public ViewEventController(ViewEventActivity viewEventActivity, String accessToken, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.accessToken = accessToken;
        this.viewEventActivity = viewEventActivity;
        this.refreshToken = refreshToken;
    }

    public void getEvent(int eventId) {
        Call<WorkingEvent> eventCall = api.getEvent(String.valueOf(eventId), "Bearer " + accessToken);

        eventCall.enqueue(new Callback<WorkingEvent>() {
            @Override
            public void onResponse(Call<WorkingEvent> call, Response<WorkingEvent> response) {
                if (response.isSuccessful()) {
                    viewEventActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(viewEventActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(viewEventActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WorkingEvent> call, Throwable t) {
                Toast.makeText(viewEventActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
