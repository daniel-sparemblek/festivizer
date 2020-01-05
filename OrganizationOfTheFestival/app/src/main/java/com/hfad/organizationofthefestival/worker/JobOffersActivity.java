package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobOffersActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    private JobOffersController jobApplyController;

    private ListView jobOffers;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_job_offers);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        jobApplyController = new JobOffersController(this, accessToken, username, refreshToken);

        jobOffers = findViewById(R.id.workerJobList);

        jobOffers.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(JobOffersActivity.this, JobApplyActivity.class);
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("refreshToken", refreshToken);
            intent1.putExtra("username", username);
            intent1.putExtra("job_id", jobList.get(position).getId());
            JobOffersActivity.this.startActivity(intent1);
        });

        jobApplyController.getJobs();
    }

    public void fillInActivity(Job[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(body));
        jobOffers.setAdapter(specializationArrayAdapter);
    }

    public List<String> jobsToStrings(Job[] jobs) {
        jobList = Arrays.asList(jobs);

        List<String> stringList = jobList.stream()
                .map(t -> t.toString())
                .collect(Collectors.toList());
        return stringList;
    }
}
