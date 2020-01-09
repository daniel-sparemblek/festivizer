package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.NewJob;
import com.hfad.organizationofthefestival.utility.Specialization;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NewJobController {

    private NewJobActivity activity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;
    private String festivalName;
    private String eventName;

    public NewJobController(NewJobActivity activity, String accessToken, String username, String refreshToken, String eventName, String festivalName) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.activity = activity;
        this.refreshToken = refreshToken;
        this.festivalName = festivalName;
        this.eventName = eventName;
    }

    public void createNewJob(NewJob newJob) {
        Call<JSONObject> call = api.createNewJob(newJob, "Bearer " + accessToken);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(activity, response.body().getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Toast.makeText(activity, "Wrong serialization!", Toast.LENGTH_SHORT).show();
                    }
                } else{
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
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSpecializations(){
        Call<List<Specialization>> call = api.getSpecializations("Bearer " + accessToken);

        call.enqueue(new Callback<List<Specialization>>() {
            @Override
            public void onResponse(Call<List<Specialization>> call, Response<List<Specialization>> response) {
                if (response.isSuccessful()){
                    activity.fillInSpinners(response.body());
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
            public void onFailure(Call<List<Specialization>> call, Throwable t) {
                Toast.makeText(activity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
