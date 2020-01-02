package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;

public class WorkerActivity extends AppCompatActivity {

    private WorkerController workerController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Worker worker;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private ListView lvSpecializations;
    private ListView lvJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("WORKER");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_profile);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        workerController = new WorkerController(this);

        workerController.getWorker(accessToken, username, worker);
    }

    public void fillInActivity(Worker worker) {
        tvName = findViewById(R.id.workerName);
        tvPhone = findViewById(R.id.workerPhone);
        tvEmail = findViewById(R.id.workerEmail);

        tvName.setText(worker.getUsername());
        tvPhone.setText(worker.getPhone());
        tvEmail.setText(worker.getEmail());

        lvJobs = findViewById(R.id.jobsList);
        ArrayAdapter<String> jobsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getJobs());
        lvJobs.setAdapter(jobsArrayAdapter);

        lvSpecializations = findViewById(R.id.specializationList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getSpecializations());
        lvSpecializations.setAdapter(specializationArrayAdapter);
    }
}
