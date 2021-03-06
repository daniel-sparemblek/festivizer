package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ActiveJobsController {

    private ActiveJobsActivity activeJobsActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public ActiveJobsController(ActiveJobsActivity activeJobsActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.activeJobsActivity = activeJobsActivity;
        this.refreshToken = refreshToken;
    }

    public void getActiveJobs(String username) {
        Call<JobApply[]> call = api.getActiveJobs(username, "0", "Bearer " + accessToken);

        call.enqueue(new Callback<JobApply[]>() {
            @Override
            public void onResponse(Call<JobApply[]> call, Response<JobApply[]> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().length);
                    activeJobsActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(activeJobsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(activeJobsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JobApply[]> call, Throwable t) {
                Toast.makeText(activeJobsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
