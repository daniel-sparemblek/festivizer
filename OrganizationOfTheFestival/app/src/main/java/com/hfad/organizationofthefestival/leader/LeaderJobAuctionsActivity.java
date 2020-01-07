package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hfad.organizationofthefestival.R;

public class LeaderJobAuctionsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    LeaderJobAuctionsController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_job_auc);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");

        controller = new LeaderJobAuctionsController(this, accessToken, refreshToken);
    }
}
