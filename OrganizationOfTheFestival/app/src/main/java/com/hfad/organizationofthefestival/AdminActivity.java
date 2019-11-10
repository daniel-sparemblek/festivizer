package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;

import com.hfad.organizationofthefestival.adapters.AdminAdapter;

import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity implements AdminConnector.AdminListener {

    private ArrayList<String> data = new ArrayList<>();     //admin username
    private ListView approvalList;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        approvalList = findViewById(R.id.approvalList);

        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");

        AdminConnector.getPendingLeaders(username, password, this);
    }

    @Override
    public void onAdminResponse(ArrayList<String> pendingLeaders) {
        data = pendingLeaders;
        AdminAdapter adapter = new AdminAdapter(this, R.layout.skroznovi, data);
        approvalList.setAdapter(adapter);
    }

    @Override
    public void onSendDecisionResponse(ServerStatus serverStatus) {
        System.out.println("RESPONSE: POZVANO");
        AdminConnector.getPendingLeaders(username, password, this);

    }

    public void adminOnClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String leaderUsername = data.get(position);

        AdminConnector.sendAdminDecision(username, password, leaderUsername, Decision.ACCEPT, this);
    }

    public void adminOnClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String leaderUsername = data.get(position);

        AdminConnector.sendAdminDecision(username, password, leaderUsername, Decision.DECLINE, this);
    }

}
