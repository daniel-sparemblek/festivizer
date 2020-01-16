package com.hfad.organizationofthefestival.worker.jobSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class WorkerJobSearchActivity extends AppCompatActivity {

    private Job job;

    private EditText etComment;
    private Button btnAddComment;
    private TextView tvJobName;
    private TextView tvJobDescription;
    private TextView tvEvent;
    private TextView tvWorkerName;
    private TextView tvStartTime;
    private TextView tvFestivalName;

    private TextView tvComment;

    private WorkerJobSearchController controller;


    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job2);

        findViews();
        btnAddComment.setVisibility(View.INVISIBLE);
        etComment.setVisibility(View.INVISIBLE);
        tvComment.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");
        String refreshToken = intent.getStringExtra("refreshToken");
        String jsonJob = intent.getStringExtra("job");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        job = gson.fromJson(jsonJob, Job.class);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller = new WorkerJobSearchController(this, accessToken, refreshToken);

        controller.getFestivalName(job.getId());

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
        tvComment = findViewById(R.id.tvorg_searchTxt);
    }

    public void fillInActivity(JobApply jobApply) {
        tvFestivalName.setText(jobApply.getEvent().getFestival().getName());
        tvEvent.setText(jobApply.getEvent().getName());
        tvJobDescription.setText(jobApply.getDescription());
        tvWorkerName.setText(jobApply.getWorker().getUsername());
        tvJobName.setText(jobApply.getName());
        tvStartTime.setText(convertTime(jobApply.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        if (jobApply.getComment() == null) {
            btnAddComment.setVisibility(View.VISIBLE);
            etComment.setVisibility(View.VISIBLE);
        } else {
            tvComment.setVisibility(View.VISIBLE);
            tvComment.setText(jobApply.getComment());
        }
        dialog.dismiss();
    }

    private ZonedDateTime convertTime(String time){
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(5, 7));
        int day = Integer.parseInt(time.substring(8, 10));
        int hour = Integer.parseInt(time.substring(11, 13));
        int minute = Integer.parseInt(time.substring(14, 16));
        int second = Integer.parseInt(time.substring(17, 19));
        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }
}
