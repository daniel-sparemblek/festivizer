package com.hfad.organizationofthefestival.worker.jobSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;

public class WorkerJobSearchActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnAddComment;
    private Job job;
    private int event;

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
    }

    private void fillInActivity(){

    }
}
