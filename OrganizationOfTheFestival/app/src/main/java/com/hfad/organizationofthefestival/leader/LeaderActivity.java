package com.hfad.organizationofthefestival.leader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hfad.organizationofthefestival.Decision;
import com.hfad.organizationofthefestival.PendingOrganizer;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.ServerStatus;
import com.hfad.organizationofthefestival.adapters.LeaderAdapter;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity implements LeaderController.LeaderListener {

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

        LeaderController.getPendingOrganizers(username, password, this);
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
        LeaderController.getPendingOrganizers(username, password, this);

    }

    // Decisions need to be fixed when JSONs get here
    public void onClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        // LeaderController.sendDecision(username, password, organizerUsername, Integer.toString(festivalId), Decision.ACCEPT, this);
    }

    public void onClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout)view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

       //  LeaderController.sendDecision(username, password,organizerUsername, Integer.toString(festivalId), Decision.DECLINE, this);

    }
}
