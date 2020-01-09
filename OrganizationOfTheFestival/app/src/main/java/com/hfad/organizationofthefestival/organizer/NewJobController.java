package com.hfad.organizationofthefestival.organizer;

import android.app.Activity;
import android.widget.Toast;

import com.hfad.organizationofthefestival.utility.EventApply;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NewJobController {

    private NewJobActivity newJobActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private Organizer organizer;
    private String festivalName;
    private String eventName;

    public NewJobController(NewJobActivity newJobActivity, String accessToken, String username, String refreshToken, String eventName, String festivalName) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.newJobActivity = newJobActivity;
        this.refreshToken = refreshToken;
        this.festivalName = festivalName;
        this.eventName = eventName;
    }

    public void createNewJob(){

    }
}
