package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.Application;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ViewApplicationController {

    private ViewApplicationActivity viewApplicationActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public ViewApplicationController(ViewApplicationActivity viewApplicationActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.viewApplicationActivity = viewApplicationActivity;
        this.refreshToken = refreshToken;
    }

    public void getApplication(int applicationId) {
        Call<Application> call = api.getApplication(String.valueOf(applicationId), "Bearer " + accessToken);

        call.enqueue(new Callback<Application>() {
            @Override
            public void onResponse(Call<Application> call, Response<Application> response) {
                if (response.isSuccessful()) {
                    viewApplicationActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(viewApplicationActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(viewApplicationActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Application> call, Throwable t) {
                Toast.makeText(viewApplicationActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
