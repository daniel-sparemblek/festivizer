package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

public class ViewEventActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderId;
    private String festivalId;
    private long eventId;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvLocation;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvOrganizerName;

    private ViewEventController viewEventController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_view_event);

        tvName = findViewById(R.id.nameOut);
        tvDesc = findViewById(R.id.descOut);
        tvLocation = findViewById(R.id.locationOut);
        tvStartTime = findViewById(R.id.startTimeOut);
        tvEndTime = findViewById(R.id.endTimeOut);
        tvOrganizerName = findViewById(R.id.organizerOut);

        viewEventController = new ViewEventController(this, accessToken, refreshToken);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getStringExtra("leader_id");
        festivalId= intent.getStringExtra("festival_id");
        eventId = intent.getLongExtra("event_id", 0);

        viewEventController = new ViewEventController(this, accessToken, refreshToken);
        viewEventController.getEvent((int)eventId);
    }

    public void fillInActivity(EventApply body) {
        tvName.setText(body.getName());
        tvDesc.setText(body.getDesc());
        tvLocation.setText(body.getLocation());
        tvStartTime.setText(body.getStartTime());
        tvEndTime.setText(body.getEndTime());
        tvOrganizerName.setText(String.valueOf(body.getOrganizer().getUsername()));
    }
}
