package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.Job;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class JobOffersController {

    private JobOffersActivity jobOffersActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public JobOffersController(JobOffersActivity jobOffersActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.jobOffersActivity = jobOffersActivity;
        this.refreshToken = refreshToken;
    }

    public void getJobs() {
        Call<Job[]> call = api.getJobs("Bearer " + accessToken);

        call.enqueue(new Callback<Job[]>() {
            @Override
            public void onResponse(Call<Job[]> call, Response<Job[]> response) {
                if (response.isSuccessful()) {
                    jobOffersActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(jobOffersActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(jobOffersActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Job[]> call, Throwable t) {
                Toast.makeText(jobOffersActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
