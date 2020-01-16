package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MyFestivalsController {

    private MyFestivalsActivity myFestivalsActivity;
    private LeaderClient leaderApi;
    private String accessToken;
    private int leaderID;
    private String refreshToken;

    public MyFestivalsController(MyFestivalsActivity myFestivalsActivity, String accessToken, int leaderID, String refreshToken) {
        leaderApi = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.accessToken = accessToken;
        this.leaderID = leaderID;
        this.myFestivalsActivity = myFestivalsActivity;
        this.refreshToken = refreshToken;
    }

    public void getFestivals(String type) {
        Call<Festival[]> leaderCall = leaderApi.getFestivals(type, leaderID, "Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Festival[]>() {
            @Override
            public void onResponse(Call<Festival[]> call, Response<Festival[]> response) {
                if (response.isSuccessful()) {
                    switch (type) {
                        case "active":
                            myFestivalsActivity.fillInActive(response.body());
                            break;
                        case "pending":
                            myFestivalsActivity.fillInPending(response.body());
                            break;
                        case "complete":
                            myFestivalsActivity.fillInCompleted(response.body());
                            break;
                    }
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(myFestivalsActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(myFestivalsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Festival[]> call, Throwable t) {
                Toast.makeText(myFestivalsActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
