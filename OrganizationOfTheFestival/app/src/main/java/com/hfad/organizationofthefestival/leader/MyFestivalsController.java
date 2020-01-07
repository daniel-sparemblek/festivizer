package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.Festivals;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MyFestivalsController {

    private MyFestivalsActivity myFestivalsActivity;
    private LeaderClient api;
    private String accessToken;
    private String leaderID;
    private String refreshToken;

    public MyFestivalsController(MyFestivalsActivity myFestivalsActivity, String accessToken, String leaderID, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.accessToken = accessToken;
        this.leaderID = leaderID;
        this.myFestivalsActivity = myFestivalsActivity;
        this.refreshToken = refreshToken;
    }

    public void getCompletedFestivals() {
        Call<Festivals> leaderCall = api.getCompletedFestivals(leaderID, "Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Festivals>() {
            @Override
            public void onResponse(Call<Festivals> call, Response<Festivals> response) {
                if (response.isSuccessful()) {
                    myFestivalsActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(myFestivalsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(myFestivalsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Festivals> call, Throwable t) {
                Toast.makeText(myFestivalsActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
