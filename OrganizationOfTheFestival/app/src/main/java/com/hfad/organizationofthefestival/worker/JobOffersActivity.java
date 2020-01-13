package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.WorkerSearchActivity;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobOffersActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private int permission;

    private JobOffersController jobApplyController;

    private ListView jobOffers;
    private List<Job> jobList;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_job_offers);

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        toolbar.setTitle("Apply for a Job");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", -1);

        jobApplyController = new JobOffersController(this, accessToken, username, refreshToken);

        jobOffers = findViewById(R.id.workerJobList);

        jobOffers.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(JobOffersActivity.this, JobApplyActivity.class);
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("refreshToken", refreshToken);
            intent1.putExtra("username", username);
            intent1.putExtra("job_id", jobList.get(position).getId());
            startActivity(intent1);
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        jobApplyController.getJobs();
    }

    public void fillInActivity(Job[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(body));
        jobOffers.setAdapter(specializationArrayAdapter);
        dialog.dismiss();
    }

    public List<String> jobsToStrings(Job[] jobs) {
        jobList = Arrays.asList(jobs);

        return jobList.stream()
                .map(Job::toString)
                .collect(Collectors.toList());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.applyForJob) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            jobApplyController.getJobs();
        } else if (id == R.id.activeJobs) {
            Intent intent = new Intent(this, ActiveJobsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.myApplications) {
            Intent intent = new Intent(this, MyApplicationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.printPass) {

        } else if (id == R.id.search) {
            Intent intent = new Intent(this, WorkerSearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.worker_profile) {
            Intent intent = new Intent(this, WorkerActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
