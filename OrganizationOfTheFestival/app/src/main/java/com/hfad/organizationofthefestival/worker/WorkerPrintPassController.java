package com.hfad.organizationofthefestival.worker;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.JobApply;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class WorkerPrintPassController {

    private WorkerPrintPassActivity workerPrintPassActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Worker worker;
    private ApplicationResponse application;

    public WorkerPrintPassController(WorkerPrintPassActivity workerPrintPassActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.workerPrintPassActivity = workerPrintPassActivity;
        this.refreshToken = refreshToken;
    }

    public void getWorkerJobs(String username) {
        Call<JobApply[]> workerCall = api.getActiveJobs(String.valueOf(username), "0", "Bearer " + accessToken);

        workerCall.enqueue(new Callback<JobApply[]>() {
            @Override
            public void onResponse(Call<JobApply[]> call, Response<JobApply[]> response) {
                if (response.isSuccessful()) {
                    workerPrintPassActivity.fillInActivity(response.body());
                    getWorker();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(workerPrintPassActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(workerPrintPassActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JobApply[]> call, Throwable t) {
                Toast.makeText(workerPrintPassActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWorker() {
        Call<Worker> workerCall = api.getWorker(username, "Bearer " + accessToken);

        workerCall.enqueue(new Callback<Worker>() {
            @Override
            public void onResponse(Call<Worker> call, Response<Worker> response) {
                if (response.isSuccessful()) {
                    worker = response.body();
                    workerPrintPassActivity.fillInWorker(worker);
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(workerPrintPassActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(workerPrintPassActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Worker> call, Throwable t) {
                Toast.makeText(workerPrintPassActivity, "Unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getApplication(JobApply job){
        System.out.println(job.getJobId());
        System.out.println(String.valueOf(job.getJobId()));
        System.out.println("KURAC1");
        Call<ApplicationResponse> applicationCall = api.getWorkerApplication(String.valueOf(job.getJobId()), "Bearer " + accessToken);
        System.out.println("KURAC2");

        applicationCall.enqueue(new Callback<ApplicationResponse>() {
            @Override
            public void onResponse(Call<ApplicationResponse> call, Response<ApplicationResponse> response) {
                System.out.println("KURAC3");
                if (response.isSuccessful()) {
                    application = response.body();
                    workerPrintPassActivity.fillInApplication(application, job);
                } else {
                    System.out.println("PENIAS");
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().toString());
                        Toast.makeText(workerPrintPassActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(workerPrintPassActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApplicationResponse> call, Throwable t) {
                System.out.println("KURAC4");
                Toast.makeText(workerPrintPassActivity, "Unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
