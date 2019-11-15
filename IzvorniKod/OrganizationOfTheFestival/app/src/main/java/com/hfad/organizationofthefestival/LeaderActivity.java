package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.adapters.LeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderActivity extends AppCompatActivity implements LeaderConnector.LeaderListener{

    ArrayList<PendingOrganizer> pendingOrganizers = new ArrayList<>();
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
    public void onGetPendingOrganizersResponse(ArrayList<String> pendingOrganizer) {

        pendingOrganizers = new ArrayList<>();
        for(String s : pendingOrganizer) {

            if(s.isEmpty()) {
                break;
            }

            String[] fragments = s.split("\\s");

            pendingOrganizers.add(new PendingOrganizer(fragments[0], fragments[2], Integer.parseInt(fragments[1])));
        }


        LeaderAdapter adapter = new LeaderAdapter(this, pendingOrganizers);
        approvalList.setAdapter(adapter);
    }

    @Override
    public void onSendDecisionResponse(ServerStatus serverStatus) {
        LeaderConnector.getPendingOrganizers(username, password, this);

    }

    public void onClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        LeaderConnector.sendDecision(username, password, organizerUsername, Integer.toString(festivalId), Decision.ACCEPT, this);
    }

    public void onClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        LeaderConnector.sendDecision(username, password,organizerUsername, Integer.toString(festivalId), Decision.DECLINE, this);

    }
}
