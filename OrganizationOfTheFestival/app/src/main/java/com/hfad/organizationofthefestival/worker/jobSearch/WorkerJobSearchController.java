package com.hfad.organizationofthefestival.worker.jobSearch;

import android.widget.Toast;

import com.hfad.organizationofthefestival.organizer.OrganizerClient;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.SimpleServerResponse;
import com.hfad.organizationofthefestival.worker.WorkerClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerJobSearchController {

    private WorkerJobSearchActivity activity;
    private String accessToken;
    private String refershToken;
    private WorkerClient workerClient;

    public WorkerJobSearchController(WorkerJobSearchActivity activity, String accessToken, String refershToken) {
        workerClient = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.activity = activity;
        this.accessToken = accessToken;
        this.refershToken = refershToken;
    }

    public void getFestivalName(int jobId){
        Call<JobApply> call = workerClient.getJob(Integer.toString(jobId), "Bearer " + accessToken);

        call.enqueue(new Callback<JobApply>() {
            @Override
            public void onResponse(Call<JobApply> call, Response<JobApply> response) {
                if (response.isSuccessful()){
                    activity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(activity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(activity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JobApply> call, Throwable t) {
                Toast.makeText(activity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewComment(int jobId, String comment){
        Map<String, String> commentMap = new HashMap<>();
        commentMap.put("comment", comment);
        Call<SimpleServerResponse> call = workerClient.addComment(Integer.toString(jobId), commentMap, "Bearer " + accessToken);

        call.enqueue(new Callback<SimpleServerResponse>() {
            @Override
            public void onResponse(Call<SimpleServerResponse> call, Response<SimpleServerResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(activity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(activity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleServerResponse> call, Throwable t) {
                Toast.makeText(activity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
