package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;

public class JobApplyActivity extends AppCompatActivity {
    private String accessToken;
    private String refreshToken;
    private String username;

    private JobApplyController jobApplyController;

    private TextView name;
    private TextView description;
    private TextView startTime;
    private TextView festivalName;
    private TextView eventName;
    private ListView specializations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_apply_for_job);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        this.jobApplyController = new JobApplyController(this, accessToken,
                username, refreshToken);

        name = findViewById(R.id.jobName);
        description = findViewById(R.id.jobDesc);
        startTime = findViewById(R.id.startTime);
        festivalName = findViewById(R.id.fest);
        eventName = findViewById(R.id.event);
        specializations = findViewById(R.id.specializationList);

        jobApplyController.getJobApplication(4);
    }

    public void fillInActivity(Job body) {
        name.setText(body.getName());
        description.setText("");
    }
}
