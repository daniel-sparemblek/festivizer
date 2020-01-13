package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    private Toolbar toolbar;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_apply_for_job);

        toolbar = findViewById(R.id.worker_toolbar);
        toolbar.setTitle("Apply for a Job");
        setSupportActionBar(toolbar);

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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        jobApplyController.getJobApplication(jobId);
    }

    public void fillInActivity(JobApply body) {
        name.setText(body.getName());
        description.setText(body.getDescription());
        startTime.setText(parseDateTime(body.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        festivalName.setText(body.getEvent().getFestival().getName());
        eventName.setText(body.getEvent().getName());

        List<String> specStrings = body.getSpecializations().stream()
                .map(Specialization::toString)
                .collect(Collectors.toList());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specStrings);
        specializations.setAdapter(specializationArrayAdapter);
        dialog.dismiss();

    }

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }

    public void applyForJob(View view) {
        Intent intent = new Intent(this, JobApplicationActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("job_id", jobId);
        startActivity(intent);
        finish();
    }
}
