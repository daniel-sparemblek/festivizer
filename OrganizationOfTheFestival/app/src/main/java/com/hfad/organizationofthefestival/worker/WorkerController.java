package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.signup.RegistrationResponse;

import org.json.JSONObject;

import java.util.List;

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

    public void getWorker(final String accessToken, String username, final Worker worker) {
        Call<Worker> call = workerClient.getWorker(username, "Bearer " + accessToken);

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
