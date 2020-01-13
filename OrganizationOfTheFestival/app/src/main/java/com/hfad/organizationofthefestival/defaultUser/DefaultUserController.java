package com.hfad.organizationofthefestival.defaultUser;

import android.widget.Toast;

import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.organizer.OrganizerClient;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.worker.Worker;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultUserController {
    private DefaultUserActivity activity;
    private DefaultUserApi api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private OrganizerClient organizerClient;


    public DefaultUserController(DefaultUserActivity activity, String accessToken, String refreshToken, String username) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DefaultUserApi.class);

        organizerClient = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.activity = activity;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public void showUserProfile(int permission) {
        if (permission == 1) {
            Call<Leader> call = api.getLeader(username, "Bearer " + accessToken);
            leaderCall(call);
        } else if (permission == 2) {
            Call<Organizer> call = api.getOrganizer(username, "Bearer " + accessToken);
            organizerCall(call);
        } else {
            Call<Worker> call = api.getWorker(username, "Bearer " + accessToken);
            workerCall(call);
        }
    }

    private void leaderCall(Call<Leader> call){
        call.enqueue(new Callback<Leader>() {
            @Override
            public void onResponse(Call<Leader> call, Response<Leader> response) {
                if (response.isSuccessful()){
                    activity.fillInActivityLeader(response.body());
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
            public void onFailure(Call<Leader> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void organizerCall(Call<Organizer> call){
        call.enqueue(new Callback<Organizer>() {
            @Override
            public void onResponse(Call<Organizer> call, Response<Organizer> response) {
                if (response.isSuccessful()){
                    activity.fillInActivityOrganizer(response.body());
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
            public void onFailure(Call<Organizer> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void workerCall(Call<Worker> call){
        call.enqueue(new Callback<Worker>() {
            @Override
            public void onResponse(Call<Worker> call, Response<Worker> response) {
                if (response.isSuccessful()){
                    activity.fillInActivityWorker(response.body());
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
            public void onFailure(Call<Worker> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOrganizerEvents(String username){
        Call<EventApply[]> call = organizerClient.getAllEvents(username, "Bearer " + accessToken);

        call.enqueue(new Callback<EventApply[]>() {
            @Override
            public void onResponse(Call<EventApply[]> call, Response<EventApply[]> response) {
                if (response.isSuccessful()){
                    activity.fillEventIds(response.body());
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
            public void onFailure(Call<EventApply[]> call, Throwable t) {
                Toast.makeText(activity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
