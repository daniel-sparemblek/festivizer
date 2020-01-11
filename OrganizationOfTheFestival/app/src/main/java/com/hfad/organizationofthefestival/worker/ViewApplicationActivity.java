package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.search.SearchActivity;
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
        viewApplicationController.getApplication(applicationId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addSpecialization) {
            Intent intent = new Intent(this, SpecializationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            this.startActivity(intent);

        } else if (id == R.id.applyForJob) {
            Intent intent = new Intent(this, JobOffersActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            this.startActivity(intent);
        } else if (id == R.id.activeJobs) {
            Intent intent = new Intent(this, ActiveJobsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            this.startActivity(intent);

        } else if (id == R.id.myApplications) {
            Intent intent = new Intent(this, MyApplicationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            this.startActivity(intent);

        } else if (id == R.id.printPass) {

        } else if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillInActivity(Application body) {
        tvPrice.setText(String.valueOf(body.getPrice()));
        tvDaysNeeded.setText(String.valueOf(body.getDuration()));
        tvNumberOfPeople.setText(String.valueOf(body.getPeopleNumber()));
        tvComment.setText(body.getComment());
        toolbar.setTitle(jobName);
    }
}
