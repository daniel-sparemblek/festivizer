package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderController {

    private LeaderActivity leaderActivity;
    private LeaderClient api;
    private String accessToken;
    private String username;
    private Leader leader;

    public LeaderController(LeaderActivity leaderActivity, String accessToken, String username) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.leaderActivity = leaderActivity;
        this.leader = null;
    }

    public Leader getData() {
        Call<Leader> leaderCall = api.getLeaderData("Barer" + accessToken, username);

        leaderCall.enqueue(new Callback<Leader>() {
            @Override
            public void onResponse(Call<Leader> call, Response<Leader> response) {
                if (response.isSuccessful()) {
                    leader = response.body();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(leaderActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(leaderActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Leader> call, Throwable t) {
                Toast.makeText(leaderActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
        return leader;
    }
}
