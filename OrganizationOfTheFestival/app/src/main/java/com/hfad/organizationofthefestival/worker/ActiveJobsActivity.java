package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveJobsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    private ListView activeJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_active_jobs);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        activeJobs = findViewById(R.id.activeJobsList);

        ActiveJobsController activeJobsController = new ActiveJobsController(this, accessToken, username, refreshToken);

        activeJobsController.getActiveJobs(username);
    }

    public void fillInActivity(JobApply[] body) {
        List<JobApply> jobs = Arrays.asList(body);

        List<String> specStrings = jobs.stream()
                .map(JobApply::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specStrings);

        activeJobs.setAdapter(specializationArrayAdapter);
    }
}
