package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity implements LeaderConnector.LeaderListener{

    private ArrayList<String> data = new ArrayList<>();
    private ListView approvalList;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        approvalList = findViewById(R.id.leaderList);
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");

        LeaderConnector.getPendingOrganizers(username, password, this);
    }

    @Override
    public void onGetPendingOrganizersResponse(ArrayList<String> pendingLeaders) {
        data = pendingLeaders;
        System.out.println(pendingLeaders);
        ArrayAdapter<String> approvalListAdapter = new ArrayAdapter<>(this, R.layout.skroznovi, data);
        approvalList.setAdapter(approvalListAdapter);
    }

    @Override
    public void onSendDecisionResponse(ServerStatus serverStatus) {

    }

}
