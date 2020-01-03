package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobApplyActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    private JobApplyController jobApplyController;

    private ListView jobOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_job_offers);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        jobApplyController = new JobApplyController(this, accessToken, username, refreshToken);

        jobOffers = findViewById(R.id.workerJobList);

        jobOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TO DO
            }
        });

        jobApplyController.getJobs();
    }

    public void fillInActivity(Job[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(body));
        jobOffers.setAdapter(specializationArrayAdapter);
    }

    public List<String> jobsToStrings(Job[] jobs) {
        List<Job> jobList = Arrays.asList(jobs);

        List<String> stringList = jobList.stream()
                .map(t -> t.toString())
                .collect(Collectors.toList());
        return stringList;
    }
}
