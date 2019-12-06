package com.hfad.organizationofthefestival.worker;

import com.hfad.organizationofthefestival.signup.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerController {

    private WorkerActivity workerActivity;

    public WorkerController(WorkerActivity workerActivity) {
        this.workerActivity = workerActivity;
    }

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://kaogrupa.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    private WorkerClient workerClient = retrofit.create(WorkerClient.class);

    public void getWorker(String accessToken, String username){
        Call<Worker> call = workerClient.getWorker(username, accessToken);

        call.enqueue(new Callback<Worker>() {
            @Override
            public void onResponse(Call<Worker> call, Response<Worker> response) {
            }

            @Override
            public void onFailure(Call<Worker> call, Throwable t) {

            }
        });
    }
}
