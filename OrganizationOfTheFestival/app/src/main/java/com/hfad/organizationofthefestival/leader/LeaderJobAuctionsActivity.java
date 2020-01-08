package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;

import java.util.List;

public class LeaderJobAuctionsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderID;
    private LeaderJobAuctionsController controller;

    private ListView jobAuctionsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_job_auc);

        jobAuctionsList = findViewById(R.id.jobAuctionList);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderID = intent.getStringExtra("leaderID");

        controller = new LeaderJobAuctionsController(this, accessToken, refreshToken, leaderID);

        controller.getJobAuctions();
    }

    public void fillInActivity(List<Application> applications){

    }
}
