package com.hfad.organizationofthefestival.festival.creation;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FestivalCreationController {

    private CreateFestivalActivity activity;
    private CreateFestivalApi api;

    public FestivalCreationController(CreateFestivalActivity activity) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CreateFestivalApi.class);
        this.activity = activity;
    }

    public void createFestival(Festival festival, String accessToken) {
        Call<CreateFestivalResponse> call =
                api.createFestival("Bearer " + accessToken, festival);

        call.enqueue(new Callback<CreateFestivalResponse>() {
            @Override
            public void onResponse(Call<CreateFestivalResponse> call, Response<CreateFestivalResponse> response) {
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
            public void onFailure(Call<CreateFestivalResponse> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
