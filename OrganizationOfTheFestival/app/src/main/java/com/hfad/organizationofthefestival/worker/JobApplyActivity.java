package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.util.List;
import java.util.stream.Collectors;

public class JobApplyActivity extends AppCompatActivity {
    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;

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
        jobId = intent.getIntExtra("job_id", 0);


        this.jobApplyController = new JobApplyController(this, accessToken,
                username, refreshToken);

        name = findViewById(R.id.jobName);
        description = findViewById(R.id.jobDesc);
        startTime = findViewById(R.id.startTime);
        festivalName = findViewById(R.id.fest);
        eventName = findViewById(R.id.event);
        specializations = findViewById(R.id.specializationList);

        jobApplyController.getJobApplication(jobId);
        System.out.println("ID2: " + jobId);
    }

    public void fillInActivity(JobApply body) {
        name.setText(body.getName());
        description.setText(body.getDescription());
        startTime.setText(body.getStartTime());
        festivalName.setText(body.getEvent().getFestivalId()+"");
        eventName.setText(body.getEvent().getName());

        List<String> specStrings = body.getSpecializations().stream()
                .map(t -> t.toString())
                .collect(Collectors.toList());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specStrings);
        specializations.setAdapter(specializationArrayAdapter);

    }
}
