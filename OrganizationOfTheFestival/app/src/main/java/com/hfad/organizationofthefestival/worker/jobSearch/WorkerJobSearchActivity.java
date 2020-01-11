package com.hfad.organizationofthefestival.worker.jobSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerJobSearchActivity extends AppCompatActivity {

    private Job job;
    private int event;

    private EditText etComment;
    private Button btnAddComment;
    private TextView tvJobName;
    private TextView tvJobDescription;
    private TextView tvEvent;
    private TextView tvWorkerName;
    private TextView tvStartTime;
    private TextView tvFestivalName;

    private WorkerJobSearchController controller;

    private int permission;
    private String searcherUsername;
    private List<Long> eventIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job2);

        findViews();
        btnAddComment.setEnabled(false);
        etComment.setEnabled(false);

        Intent intent = getIntent();
        permission = intent.getIntExtra("searcherPermission", 0);
        String accessToken = intent.getStringExtra("accessToken");
        String refreshToken = intent.getStringExtra("refreshToken");
        String jsonJob = intent.getStringExtra("job");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        searcherUsername = intent.getStringExtra("searcherUsername");
        job = gson.fromJson(jsonJob, Job.class);

        controller = new WorkerJobSearchController(this, accessToken, refreshToken);
        controller.getFestivalName(job.getId());

        if (permission == 2){
            controller.getOrganizerEvents(searcherUsername);
        }

        btnAddComment.setOnClickListener(v -> {
            String comment = etComment.getText().toString();
            controller.addNewComment(job.getId(), comment);
        });
    }

    private void findViews() {
        etComment = findViewById(R.id.org_searchTxt);
        btnAddComment = findViewById(R.id.add_comment);
        tvEvent = findViewById(R.id.org_event);
        tvJobDescription = findViewById(R.id.jobDesc);
        tvWorkerName = findViewById(R.id.org_workerName);
        tvJobName = findViewById(R.id.jobName);
        tvStartTime = findViewById(R.id.org_startTime);
        tvFestivalName = findViewById(R.id.org_fest);
    }

    public void fillInActivity(JobApply jobApply) {
        tvFestivalName.setText(jobApply.getEvent().getFestival().getName());
        tvEvent.setText(jobApply.getEvent().getName());
        tvJobDescription.setText(jobApply.getDescription());
        tvWorkerName.setText(jobApply.getWorker().getUsername());
        tvJobName.setText(jobApply.getName());
        tvStartTime.setText(jobApply.getStartTime());
        if (permission == 2) {
            if (eventIds.contains(jobApply.getEvent().getEventId())){
                btnAddComment.setEnabled(true);
                etComment.setEnabled(true);
            }
        }
    }

    public void fillEventIds(EventApply[] events){
        eventIds = Arrays.stream(events)
                .map(EventApply::getEventId)
                .collect(Collectors.toList());
    }
}
