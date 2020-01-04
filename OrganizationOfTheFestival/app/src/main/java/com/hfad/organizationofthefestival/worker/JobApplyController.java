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

public class JobApplyController {

    private JobApplyActivity jobApplyActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public JobApplyController(JobApplyActivity jobApplyActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.jobApplyActivity = jobApplyActivity;
        this.refreshToken = refreshToken;
    }


    public void getJobApplication(int jobId) {
        Call<JobApply> call = api.getJob(String.valueOf(jobId), "Bearer " + accessToken);

        call.enqueue(new Callback<JobApply>() {
            @Override
            public void onResponse(Call<JobApply> call, Response<JobApply> response) {
                if (response.isSuccessful()) {
                    jobApplyActivity.fillInActivity(response.body());
                } else {
                    try {
                        System.out.println(response.errorBody());
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(jobApplyActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(jobApplyActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JobApply> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(jobApplyActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
