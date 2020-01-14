package com.hfad.organizationofthefestival.organizer;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewEventController {

    private ViewEventActivity viewEventActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;
    private List<String> orderList = new LinkedList<>();

    public ViewEventController(ViewEventActivity viewEventActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.viewEventActivity = viewEventActivity;
        this.refreshToken = refreshToken;
    }

    public void getEvent(int eventId) {
        Call<EventApply> eventCall = api.getEvent(String.valueOf(eventId), "Bearer " + accessToken);

        eventCall.enqueue(new Callback<EventApply>() {
            @Override
            public void onResponse(Call<EventApply> call, Response<EventApply> response) {
                if (response.isSuccessful()) {
                    viewEventActivity.fillInActivity(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(viewEventActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(viewEventActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventApply> call, Throwable t) {
                Toast.makeText(viewEventActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getJobs(int eventId) {
        Call<Job[]> call = api.getJobsForEvent(String.valueOf(eventId), "Bearer " + accessToken);

        call.enqueue(new Callback<Job[]>() {
            @Override
            public void onResponse(Call<Job[]> call, Response<Job[]> response) {
                if (response.isSuccessful()) {
                    viewEventActivity.fillInOrderList(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(viewEventActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(viewEventActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Job[]> call, Throwable t) {
                Toast.makeText(viewEventActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateJobOrders(int event_id, HashMap<String, String>orderMap) {
        Call<Void> call = api.updateJobOrders(Integer.toString(event_id), orderMap, "Bearer " + accessToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(viewEventActivity, "Successfully updated job order, restart to see!", Toast.LENGTH_SHORT).show();
                    getJobs(event_id);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void saveList(List<String> orderList) {
        this.orderList = orderList;
    }


}
