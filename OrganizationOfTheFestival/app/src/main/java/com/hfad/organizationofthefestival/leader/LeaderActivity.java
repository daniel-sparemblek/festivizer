package com.hfad.organizationofthefestival.leader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hfad.organizationofthefestival.organizer.PendingOrganizer;
import com.hfad.organizationofthefestival.R;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity {

    ArrayList<PendingOrganizer> pendingOrganizers = new ArrayList<>();
    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;

    private LeaderController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obsolete_activity_leader);

        approvalList = findViewById(R.id.leaderList);
        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");

        controller = new LeaderController(this, accessToken, username);



    }


    // Decisions need to be fixed when JSONs get here
    public void onClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        // LeaderController.sendDecision(username, password, organizerUsername, Integer.toString(festivalId), Decision.ACCEPT, this);
    }

    public void onClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        //  LeaderController.sendDecision(username, password,organizerUsername, Integer.toString(festivalId), Decision.DECLINE, this);

    }
}
