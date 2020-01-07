package com.hfad.organizationofthefestival.organizer;

import android.widget.Toast;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.FestivalsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplyFestController {
    private ApplyFestActivity applyFestActivity;
    private OrganizerClient api;
    private String accessToken;
    private String username;
    private String refreshToken;
    private char c = 0x2714;

    public ApplyFestController(ApplyFestActivity applyFestActivity, String accessToken, String username, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrganizerClient.class);

        this.accessToken = accessToken;
        this.username = username;
        this.applyFestActivity = applyFestActivity;
        this.refreshToken = refreshToken;
    }


    public List<String> formatFestivals(FestivalsResponse[] festivals) {
        List<String> result = new ArrayList<>();

        for (FestivalsResponse festival : festivals) {
            if (festival.getOrgStatus() == 1) {
                result.add(festival.toString() + " " + c);
            } else if (festival.getOrgStatus() == 0) {
                result.add(festival.toString());
            }
        }

        return result;
    }

    public void fetchFestivals() {
        Call<FestivalsResponse[]> festivalsCall = api.getFestivals("Bearer " + accessToken);

        festivalsCall.enqueue(new Callback<FestivalsResponse[]>() {
            @Override
            public void onResponse(Call<FestivalsResponse[]> call, Response<FestivalsResponse[]> response) {
                if (response.isSuccessful()) {
                    applyFestActivity.fillInActivity(response.body());
                }
            }

            @Override
            public void onFailure(Call<FestivalsResponse[]> call, Throwable t) {
                Toast.makeText(applyFestActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apply(Long festId) {
        Call<Void> applyCall = api.apply("Bearer " + accessToken, festId.toString());

        applyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(applyFestActivity, "You have applied!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(applyFestActivity, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(applyFestActivity, "Unable to show error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(applyFestActivity, "Something went terribly wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
