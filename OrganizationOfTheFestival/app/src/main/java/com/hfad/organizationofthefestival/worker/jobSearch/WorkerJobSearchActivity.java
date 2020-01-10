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
import com.hfad.organizationofthefestival.utility.Job;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job2);

        findViews();
        Intent intent = getIntent();
        event = intent.getIntExtra("event", 0);
        String jsonJob = intent.getStringExtra("job");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        job = gson.fromJson(jsonJob, Job.class);
    }

    private void findViews(){
        etComment = findViewById(R.id.org_searchTxt);
        btnAddComment = findViewById(R.id.add_comment);
        tvEvent = findViewById(R.id.org_event);
        tvJobDescription = findViewById(R.id.jobDesc);
        tvWorkerName = findViewById(R.id.org_workerName);
        tvJobName = findViewById(R.id.jobName);
        tvStartTime = findViewById(R.id.org_startTime);
        tvFestivalName = findViewById(R.id.org_fest);
    }

    public void fillInActivity(String festivalName) {
    }
}
