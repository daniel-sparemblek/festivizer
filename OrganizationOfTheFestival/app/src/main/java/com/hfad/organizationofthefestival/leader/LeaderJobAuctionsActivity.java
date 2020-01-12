package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.ApplicationAdapter;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.Arrays;
import java.util.List;

public class LeaderJobAuctionsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderID;
    private LeaderJobAuctionsController controller;

    private ListView jobAuctionsList;

    private List<ApplicationResponse> applicationList;

    private ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getJobAuctions();
    }

    public void fillInActivity(ApplicationResponse[] applications){
        applicationList = Arrays.asList(applications);

        ApplicationAdapter applicationAdapter = new ApplicationAdapter(this, R.layout.application_row_layout, applicationList);

        jobAuctionsList.setAdapter(applicationAdapter);

        dialog.dismiss();

    }
}
