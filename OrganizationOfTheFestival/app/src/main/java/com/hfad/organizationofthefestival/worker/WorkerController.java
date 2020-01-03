package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerController {

    private WorkerActivity workerActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Worker worker;

    public WorkerController(WorkerActivity workerActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.workerActivity = workerActivity;
        this.refreshToken = refreshToken;
    }

    public void getWorker() {
        Call<Worker> call = api.getWorker(username, "Bearer " + accessToken);

        call.enqueue(new Callback<Worker>() {
            @Override
            public void onResponse(Call<Worker> call, Response<Worker> response) {
                if (response.isSuccessful()) {
                    workerActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(workerActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(workerActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Worker> call, Throwable t) {
                Toast.makeText(workerActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
