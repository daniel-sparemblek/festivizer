package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvDescription;
    private TextView tvStartTime;
    private TextView tvFestivalName;
    private TextView tvEventName;
    private ListView lvSpecializations;

    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;

    private JobProfileController jobProfileController;
    private List<JobApply> jobList;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_job_profile);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobId = intent.getIntExtra("job_id", 0);

        tvName = findViewById(R.id.jobName);
        tvDescription = findViewById(R.id.jobDesc);
        tvStartTime = findViewById(R.id.startTime);
        tvEventName = findViewById(R.id.event);
        tvFestivalName = findViewById(R.id.fest);
        lvSpecializations = findViewById(R.id.specializationList);

        jobProfileController = new JobProfileController(this, accessToken, username, refreshToken);
        System.out.println("JOB_ID " + jobId);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        jobProfileController.getJobApplication(jobId);
    }

    public void fillInActivity(JobApply body) {
        jobList = Arrays.asList(body);

        tvName.setText(body.getName());
        tvFestivalName.setText(body.getEvent().getFestival().getName());
        tvEventName.setText(body.getEvent().getName());
        tvStartTime.setText(body.getStartTime());
        tvDescription.setText(body.getDescription());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specializationsToStrings(body.getSpecializations()));
        lvSpecializations.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
    }

    public List<String> specializationsToStrings(List<Specialization> specializations) {
        List<String> stringList = specializations.stream()
                .map(Specialization::getName)
                .collect(Collectors.toList());
        return stringList;
    }
}
