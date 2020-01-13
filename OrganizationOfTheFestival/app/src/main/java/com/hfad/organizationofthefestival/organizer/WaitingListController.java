package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WaitingListController {

    private WaitingListActivity waitingListActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private int jobId;

    public WaitingListController(WaitingListActivity waitingListActivity, String accessToken, String username, String refreshToken, int jobId) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.waitingListActivity = waitingListActivity;
        this.refreshToken = refreshToken;
        this.jobId = jobId;
    }

    public void setApplicationStatus() {
        Call<ApplicationResponse[]> applicationResponseCall = api.getApplications(jobId, "Bearer " + accessToken);

        applicationResponseCall.enqueue(new Callback<ApplicationResponse[]>() {
            @Override
            public void onResponse(Call<ApplicationResponse[]> call, Response<ApplicationResponse[]> response) {
                if(response.isSuccessful()) {
                    waitingListActivity.fillInActivity(response.body());
                } else {
                    try {
                        Toast.makeText(waitingListActivity, "Something went wrong: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(waitingListActivity, "Unable to parse error message", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ApplicationResponse[]> call, Throwable t) {
                Toast.makeText(waitingListActivity, "Fatal error: " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
