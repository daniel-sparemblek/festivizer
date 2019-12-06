package com.hfad.organizationofthefestival.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.AdminAdapter;

import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity {

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

        //AdminConnector.getPendingLeaders(username, password, this);
    }

    public void adminOnClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String leaderUsername = data.get(position);

        //AdminConnector.sendDecision(username, password, leaderUsername, Decision.ACCEPT, this);
    }

    public void adminOnClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String leaderUsername = data.get(position);

        //AdminConnector.sendDecision(username, password, leaderUsername, Decision.DECLINE, this);
    }

}
