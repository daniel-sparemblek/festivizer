package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Application;

public class JobApplicationActivity extends AppCompatActivity {

    private TextView tvPrice;
    private TextView tvNumberOfPeople;
    private TextView tvTime;
    private TextView tvComment;
    private Button btnApply;

    private String accessToken;
    private String refreshToken;
    private String username;
    private int jobId;

    private JobApplicationController jobApplicationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_application);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobId = intent.getIntExtra("job_id", 0);

        tvPrice = findViewById(R.id.price);
        tvNumberOfPeople = findViewById(R.id.numbOfPeople);
        tvTime = findViewById(R.id.timeNeeded);
        tvComment = findViewById(R.id.comment);
        btnApply = findViewById(R.id.applyForJob);
        jobApplicationController = new JobApplicationController(this, accessToken, username, refreshToken);
    }

    public void applyForJob(View view) {
        double price;
        int numberOfPeople;
        int time;

        try {
            price = Double.parseDouble(tvPrice.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please provide correct input for price, e.g. 8997.65", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            numberOfPeople = Integer.parseInt(tvNumberOfPeople.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please provide correct input for number of people, e.g. 7", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            time = Integer.parseInt(tvTime.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please provide correct input for time, e.g. 7", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = tvComment.getText().toString();

        Application application = new Application(jobId, price, comment, time, numberOfPeople);

        jobApplicationController.createJobApplication(application);
    }
}
