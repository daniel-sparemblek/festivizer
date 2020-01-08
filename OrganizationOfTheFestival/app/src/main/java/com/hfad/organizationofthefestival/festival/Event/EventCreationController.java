package com.hfad.organizationofthefestival.festival.Event;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventCreationController {

    private CreateEventActivity activity;
    private CreateEventApi api;

    public EventCreationController(CreateEventActivity activity) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CreateEventApi.class);
        this.activity = activity;
    }

    public void createFestival(Festival festival, String accessToken) {
        Call<CreateEventResponse> call =
                api.createFestival("Bearer " + accessToken, festival);

        call.enqueue(new Callback<CreateEventResponse>() {
            @Override
            public void onResponse(Call<CreateEventResponse> call, Response<CreateEventResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(activity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(activity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateEventResponse> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
