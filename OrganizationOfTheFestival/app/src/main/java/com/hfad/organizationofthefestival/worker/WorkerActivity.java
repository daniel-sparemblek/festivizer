package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hfad.organizationofthefestival.R;

public class WorkerActivity extends AppCompatActivity {

    private WorkerController workerController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Worker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obsolete_activity_worker);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        workerController = new WorkerController(this);

        workerController.getWorker(accessToken, username, worker);

        //TODO all info can be filled up
    }
}
