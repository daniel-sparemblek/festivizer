package com.hfad.organizationofthefestival.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.AdminAdapter;
import com.hfad.organizationofthefestival.leader.Leader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdminActivity extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<>();     //admin username
    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;

    private AdminController adminController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        approvalList = findViewById(R.id.approvalList);

        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");

        adminController = new AdminController(this, accessToken, username, refreshToken);
        adminController.getData();

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

    public void fillInActivity(Admin admin) {
        List<Leader> leaders = admin.getLeaders();

        System.out.println(leaders);

        List<String> stringList = leadersToStrings(leaders);

        AdminAdapter adminAdapter = new AdminAdapter(this, R.layout.admin_row_layout, stringList);
        approvalList.setAdapter(adminAdapter);

    }

    public List<String> leadersToStrings(List<Leader> leaders) {
        return leaders.stream()
                .map(t -> t.getUsername())
                .collect(Collectors.toList());
    }
}
