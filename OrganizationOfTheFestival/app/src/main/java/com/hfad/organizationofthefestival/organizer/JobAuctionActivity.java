package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.JobApply;

public class JobAuctionActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;
    private JobAuctionController controller;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvEvent;
    private TextView tvFestival;
    private TextView tvStartTime;
    private TextView tvWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job1);

        tvName = findViewById(R.id.jobName);
        tvDesc = findViewById(R.id.jobDesc);
        tvEvent = findViewById(R.id.org_event);
        tvFestival = findViewById(R.id.org_fest);
        tvStartTime = findViewById(R.id.org_startTime);
        tvWorker = findViewById(R.id.org_workerName);

        getDataFromIntent();

        controller = new JobAuctionController(this, accessToken, jobId, refreshToken);

        controller.getJobInfo();
    }

    private void getViews() {
        tvName = findViewById(R.id.jobName);
        tvDesc = findViewById(R.id.jobDesc);
        tvEvent = findViewById(R.id.org_event);
        tvFestival = findViewById(R.id.org_fest);
        tvStartTime = findViewById(R.id.org_startTime);
        tvWorker = findViewById(R.id.org_workerName);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobId = intent.getIntExtra("jobId", 0);
    }

    public void fillInActivity(JobApply job) {
        tvName.setText(job.getName());
        tvDesc.setText(job.getDescription());
        tvEvent.setText(job.getEvent().getName());
        tvFestival.setText(job.getEvent().getFestival().getName());
        tvStartTime.setText(job.getStartTime());
        if(job.getWorker() == null) {
            tvWorker.setText("NULL BRAT");
        } else {
            tvWorker.setText(job.getWorker().getUsername());
        }
    }
}
