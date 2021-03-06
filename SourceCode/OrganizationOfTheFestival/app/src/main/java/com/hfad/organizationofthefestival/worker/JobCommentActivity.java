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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobCommentActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvDescription;
    private TextView tvStartTime;
    private TextView tvFestivalName;
    private TextView tvEventName;
    private TextView tvComment;
    private ListView lvSpecializations;

    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;

    private JobCommentController jobCommentController;
    private List<JobApply> jobList;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_comment);

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
        tvComment = findViewById(R.id.comment);
        lvSpecializations = findViewById(R.id.specializationList);

        jobCommentController = new JobCommentController(this, accessToken, username, refreshToken);
        System.out.println("JOB_ID " + jobId);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        jobCommentController.getJobApplication(jobId);
    }

    public void fillInActivity(JobApply body) {
        jobList = Arrays.asList(body);

        tvName.setText(body.getName());
        tvFestivalName.setText(body.getEvent().getFestival().getName());
        tvEventName.setText(body.getEvent().getName());
        tvStartTime.setText(parseDateTime(body.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvDescription.setText(body.getDescription());
        tvComment.setText(body.getComment());

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

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }
}
