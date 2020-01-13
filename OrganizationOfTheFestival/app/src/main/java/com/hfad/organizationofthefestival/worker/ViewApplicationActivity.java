package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Application;

public class ViewApplicationActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String jobName;
    private int applicationId;

    private ViewApplicationController viewApplicationController;

    private TextView tvPrice;
    private TextView tvNumberOfPeople;
    private TextView tvDaysNeeded;
    private TextView tvComment;
    private Toolbar toolbar;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_view_application);

        toolbar = findViewById(R.id.worker_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobName = intent.getStringExtra("job_name");
        applicationId = intent.getIntExtra("app_id", 0);

        tvPrice = findViewById(R.id.worker_tv_price);
        tvNumberOfPeople = findViewById(R.id.worker_tv_num_of_people);
        tvDaysNeeded = findViewById(R.id.worker_tv_days_needed);
        tvComment = findViewById(R.id.worker_tv_comment);

        viewApplicationController = new ViewApplicationController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        viewApplicationController.getApplication(applicationId);
    }

    public void fillInActivity(Application body) {
        tvPrice.setText(String.valueOf(body.getPrice()));
        tvDaysNeeded.setText(String.valueOf(body.getDuration()));
        tvNumberOfPeople.setText(String.valueOf(body.getPeopleNumber()));
        tvComment.setText(body.getComment());
        toolbar.setTitle(jobName);

        dialog.dismiss();
    }
}
