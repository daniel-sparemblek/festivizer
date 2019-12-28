package com.hfad.organizationofthefestival.leader;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.organizer.PendingOrganizer;
import com.hfad.organizationofthefestival.R;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity {

    private TextView tvLeaderName;
    private TextView tvLeaderEmail;
    private TextView tvPhone;
    private ImageView ivProfilePicture;
    private ListView lvFestivalList;

    ArrayList<PendingOrganizer> pendingOrganizers = new ArrayList<>();
    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;

    private Leader leader;

    private LeaderController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_profile);

        tvLeaderEmail = findViewById(R.id.leaderEmail);
        tvLeaderName = findViewById(R.id.leaderName);
        // change festival name to phone when activity is changed
        tvPhone = findViewById(R.id.festivalName);
        // change festival logo to profile picture when activity is changed
        ivProfilePicture = findViewById(R.id.festivalLogo);

        approvalList = findViewById(R.id.leaderList);
        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");

        controller = new LeaderController(this, accessToken, username, refreshToken);

        leader = controller.getData();
        fillInActivity(leader);

    }

    private void fillInActivity(Leader leader){
        tvLeaderName.setText(leader.getUsername());
        tvLeaderEmail.setText(leader.getEmail());
        ivProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(leader.getPicture().getBytes(),
                0, leader.getPicture().length()));
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
