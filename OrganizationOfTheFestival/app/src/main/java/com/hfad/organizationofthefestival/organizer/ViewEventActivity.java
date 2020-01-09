package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

public class ViewEventActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvFestivalName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvLocation;
    private TextView tvDesc;
    private Button createJob;

    private String accessToken;
    private String refreshToken;
    private String username;
    private long eventId;

    private ViewEventController viewEventController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_event);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        eventId = intent.getLongExtra("event_id", 0);

        tvName = findViewById(R.id.org_event_name);
        tvFestivalName = findViewById(R.id.org_festival);
        tvStartTime = findViewById(R.id.org_startTime);
        tvEndTime = findViewById(R.id.org_endTime);
        tvLocation = findViewById(R.id.org_location);
        tvDesc = findViewById(R.id.org_desc);
        createJob = findViewById(R.id.org_newJob);

        viewEventController = new ViewEventController(this, accessToken, username, refreshToken);
        viewEventController.getEvent((int)eventId);
    }

    public void fillInActivity(EventApply body) {
        tvName.setText(body.getName());
        tvFestivalName.setText(String.valueOf(body.getFestival().getName()));
        tvDesc.setText(body.getDesc());
        tvLocation.setText(body.getLocation());
        tvStartTime.setText(body.getStartTime());
        tvEndTime.setText(body.getEndTime());
    }
}
