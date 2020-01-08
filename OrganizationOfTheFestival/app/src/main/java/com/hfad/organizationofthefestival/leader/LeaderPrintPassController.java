package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class LeaderPrintPassController {

    private PrintPassActivity printPassActivity;
    private LeaderClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Leader leader;

    public LeaderPrintPassController(PrintPassActivity printPassActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.printPassActivity = printPassActivity;
        this.refreshToken = refreshToken;
    }

    public void getLeaderFestivals(int leaderId) {
        Call<Festival[]> leaderCall = api.getLeaderFestivals(String.valueOf(leaderId), "Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Festival[]>() {
            @Override
            public void onResponse(Call<Festival[]> call, Response<Festival[]> response) {
                if (response.isSuccessful()) {
                    printPassActivity.fillInActivity(response.body());
                    getLeader();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(printPassActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(printPassActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Festival[]> call, Throwable t) {
                Toast.makeText(printPassActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLeader() {
        Call<Leader> leaderCall = api.getLeaderData(username, "Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Leader>() {
            @Override
            public void onResponse(Call<Leader> call, Response<Leader> response) {
                if (response.isSuccessful()) {
                    leader = response.body();
                    printPassActivity.fillInLeader(leader);
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(printPassActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(printPassActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Leader> call, Throwable t) {
                Toast.makeText(printPassActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
