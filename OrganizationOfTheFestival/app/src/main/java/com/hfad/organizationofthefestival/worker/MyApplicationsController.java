package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.Application;
import com.hfad.organizationofthefestival.utility.WorkersApplication;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MyApplicationsController {

    private MyApplicationsActivity myApplicationsActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;

    public MyApplicationsController(MyApplicationsActivity myApplicationsActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.myApplicationsActivity = myApplicationsActivity;
        this.refreshToken = refreshToken;
    }

    public void getWorkerApplications() {
        Call<WorkersApplication[]> call = api.getWorkerApplications(username, "Bearer " + accessToken);

        call.enqueue(new Callback<WorkersApplication[]>() {
            @Override
            public void onResponse(Call<WorkersApplication[]> call, Response<WorkersApplication[]> response) {
                if (response.isSuccessful()) {
                    myApplicationsActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(myApplicationsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(myApplicationsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WorkersApplication[]> call, Throwable t) {
                Toast.makeText(myApplicationsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
