package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hfad.organizationofthefestival.adapters.LeaderAdapter;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity implements LeaderConnector.LeaderListener{

    private ArrayList<String> data = new ArrayList<>();
    private ListView approvalList;

    private String username;
    private String password;

    private Button btnAcceptOrganizer = findViewById(R.id.btnAcceptOrganizer);
    private Button btnDeclineOrganizer = findViewById(R.id.btnDeclineOrganizer);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        approvalList = findViewById(R.id.leaderList);
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");

        LeaderConnector.getPendingOrganizers(username, password, this);



        btnAcceptOrganizer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //LeaderConnector.sendDecision();
            }
        });

        btnDeclineOrganizer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //LeaderConnector.sendDecision();
            }
        });
    }


    @Override
    public void onGetPendingOrganizersResponse(ArrayList<String> pendingLeaders) {

        ArrayList<PendingOrganizer> pendingOrganizers = new ArrayList<>();

        for(String s : pendingLeaders) {
            String[] fragments = s.split("\\s");

            pendingOrganizers.add(new PendingOrganizer(fragments[0], fragments[2], Integer.parseInt(fragments[1])));
        }


        LeaderAdapter adapter = new LeaderAdapter(this, R.layout.skroznovi, pendingOrganizers);
        approvalList.setAdapter(adapter);
    }

    @Override
    public void onSendDecisionResponse(ServerStatus serverStatus) {

    }


    public void onChoice(View v) {
        
    }
}
