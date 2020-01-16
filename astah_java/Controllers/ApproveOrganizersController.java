package com.hfad.organizationofthefestival.leader;

import android.widget.Toast;

import com.hfad.organizationofthefestival.organizer.Organizer;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApproveOrganizersController {

    private ApproveOrganizersActivity approveOrganizersActivity;
    private LeaderClient api;
    private String accessToken;
    private String festivalId;
    private String refreshToken;

    public ApproveOrganizersController(ApproveOrganizersActivity approveOrganizersActivity, String accessToken, String festivalId, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderClient.class);


        this.accessToken = accessToken;
        this.festivalId = festivalId;
        this.approveOrganizersActivity = approveOrganizersActivity;
        this.refreshToken = refreshToken;
    }

    public void getUnapprovedOrganizers() {
        Call<Organizer[]> festivalCall = api.getUnapprovedOrganizers(festivalId, "Bearer " + accessToken);

        festivalCall.enqueue(new Callback<Organizer[]>() {
            @Override
            public void onResponse(Call<Organizer[]> call, Response<Organizer[]> response) {
                if (response.isSuccessful()) {
                    approveOrganizersActivity.fillInActivity(response.body());
                } else {
                    Toast.makeText(approveOrganizersActivity, "KAWABOONGAAA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Organizer[]> call, Throwable t) {
                Toast.makeText(approveOrganizersActivity, "AJMO KRAGUJEVAC", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void putDecision(int organizerId, int decision) {
        HashMap<String, String> body = new HashMap<>();
        body.put("organizer_id", String.valueOf(organizerId));
        body.put("decision", String.valueOf(decision));

        Call<Void> festivalCall = api.putDecision(festivalId, body, "Bearer " + accessToken);

        festivalCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String dec = "";
                    if (decision == 2) {
                        dec = "accepted";
                    } else {
                        dec = "rejected";
                    }
                    Toast.makeText(approveOrganizersActivity, "Successfully " + dec + " organizer!", Toast.LENGTH_SHORT).show();
                    getUnapprovedOrganizers();
                } else {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(approveOrganizersActivity, "AJMO KRAGUJEVAC", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
