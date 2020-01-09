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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
        tvStartTime.setText(parseDateTime(body.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvEndTime.setText(parseDateTime(body.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }
}
