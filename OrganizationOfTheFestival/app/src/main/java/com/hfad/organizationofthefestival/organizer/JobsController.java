package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.WorkersAuction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobsController {
    private JobsActivity jobsActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;

    public JobsController(JobsActivity jobsActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.jobsActivity = jobsActivity;
        this.refreshToken = refreshToken;
    }


    public void getJobs() {
        Call<JobApply[]> jobsCall = api.getAllJobs(username, "Bearer " + accessToken);

        jobsCall.enqueue(new Callback<JobApply[]>() {
            @Override
            public void onResponse(Call<JobApply[]> call, Response<JobApply[]> response) {
                if(response.isSuccessful()) {
                    jobsActivity.fillInJobs(response.body());
                }
            }

            @Override
            public void onFailure(Call<JobApply[]> call, Throwable t) {

            }
        });
    }

    public void getAuctionedJobs() {
        Call<WorkersAuction[]> jobsCall = api.getAuctionedJobs(username, "Bearer " + accessToken);

        jobsCall.enqueue(new Callback<WorkersAuction[]>() {
            @Override
            public void onResponse(Call<WorkersAuction[]> call, Response<WorkersAuction[]> response) {
                if(response.isSuccessful()) {
                    jobsActivity.fillInAuctions(response.body());
                }
            }

            @Override
            public void onFailure(Call<WorkersAuction[]> call, Throwable t) {

            }
        });
    }

    public List<String> formatJobs(JobApply[] jobs) {
        List<String> result = new ArrayList<>();

        for(JobApply job : jobs) {
            result.add(job.getName());
        }

        return result;
    }

    public List<String> formatAuctions(WorkersAuction[] workersAuctions) {
        List<String> result = new ArrayList<>();

        for(WorkersAuction workersAuction : workersAuctions) {
            result.add(workersAuction.getJob().getName());
        }

        return result;
    }
}
