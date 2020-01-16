package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.OnAuctionResponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class JobAuctionActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;
    private JobAuctionController jobAuctionController;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvEvent;
    private TextView tvFestival;
    private TextView tvStartTime;
    private TextView tvWorker;
    private Button btnCreateAuction;

    private JobApply job;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job1);

        tvName = findViewById(R.id.jobName);
        tvDesc = findViewById(R.id.jobDesc);
        tvEvent = findViewById(R.id.org_event);
        tvFestival = findViewById(R.id.org_fest);
        tvStartTime = findViewById(R.id.org_startTime);
        tvWorker = findViewById(R.id.org_workerName);
        btnCreateAuction = findViewById(R.id.org_auction);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        getDataFromIntent();

        jobAuctionController = new JobAuctionController(this, accessToken, jobId, refreshToken);

        jobAuctionController.getJobInfo();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobId = intent.getIntExtra("jobId", 0);
    }

    public void fillInActivity(JobApply job) {
        this.job = job;
        tvName.setText(job.getName());
        tvDesc.setText(job.getDescription());
        tvEvent.setText(job.getEvent().getName());
        tvFestival.setText(job.getEvent().getFestival().getName());
        tvStartTime.setText(parseDateTime(job.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        if (job.getWorker() == null) {
            tvWorker.setText("Not assigned");
        } else {
            tvWorker.setText(job.getWorker().getUsername());
        }
        jobAuctionController.hasAuction(jobId);
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

    public void createAuction(View view) {
        jobAuctionController.createAuction(job.getJobId());
    }

    public void blockButton() {
        btnCreateAuction.setEnabled(false);
        dialog.dismiss();
    }

    public void setButton(OnAuctionResponse body) {
        System.out.println("TUUUUUUUUU");
        boolean hasAuction = body.isOnAuction;

        if (hasAuction) {
            blockButton();
            return;
        }
        dialog.dismiss();
    }
}
