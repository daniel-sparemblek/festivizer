package com.hfad.organizationofthefestival.worker;

import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpecializationsController {

    private SpecializationsActivity specializationsActivity;
    private WorkerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Worker worker;



    public SpecializationsController(SpecializationsActivity specializationsActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorkerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.specializationsActivity = specializationsActivity;
        this.refreshToken = refreshToken;
    }


    public void getSpecializations() {
        Call<Specialization[]> call = api.getSpecializations("Bearer " + accessToken);

        call.enqueue(new Callback<Specialization[]>() {
            @Override
            public void onResponse(Call<Specialization[]> call, Response<Specialization[]> response) {
                if (response.isSuccessful()) {
                    specializationsActivity.fillInActivity(response.body());

                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(specializationsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(specializationsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Specialization[]> call, Throwable t) {
                Toast.makeText(specializationsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addSpecialization(String specName) {
        HashMap<String, String> body = new HashMap<>();
        body.put("name", specName);
        Call<Void> call = api.createSpecialization(body, "Bearer " + accessToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(specializationsActivity, "Successfully created specialization!", Toast.LENGTH_SHORT).show();
                    getSpecializations();

                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(specializationsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(specializationsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(specializationsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchSpecialization(String searched) {
        HashMap<String, String> body = new HashMap<>();
        body.put("search", searched);
        Call<Specialization[]> call = api.searchSpecialization(body, "Bearer " + accessToken);

        call.enqueue(new Callback<Specialization[]>() {
            @Override
            public void onResponse(Call<Specialization[]> call, Response<Specialization[]> response) {
                if (response.isSuccessful()) {
                    specializationsActivity.fillInActivity(response.body());

                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(specializationsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(specializationsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Specialization[]> call, Throwable t) {
                Toast.makeText(specializationsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addSpecializationToWorker(Specialization specialization) {
        Call<Void> call = api.addSpecialization(String.valueOf(specialization.getSpecializationId()), "Bearer " + accessToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(specializationsActivity, "Specialization successfully bound to worker!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(specializationsActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(specializationsActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(specializationsActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
