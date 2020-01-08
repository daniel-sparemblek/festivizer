package com.hfad.organizationofthefestival.admin;

import android.app.Activity;
import android.widget.Toast;

import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.leader.LeaderActivity;
import com.hfad.organizationofthefestival.leader.LeaderClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminController {

    private AdminActivity adminActivity;
    private AdminClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public AdminController(AdminActivity adminActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AdminClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.adminActivity = adminActivity;
        this.refreshToken = refreshToken;
    }

    public void getData() {
        Call<Admin> leaderCall = api.getAdmin("Bearer " + accessToken);

        leaderCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    adminActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(adminActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(adminActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Toast.makeText(adminActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
