package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Event.CreateEventActivity;
import com.hfad.organizationofthefestival.festival.Festival;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LeaderFestivalActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String festivalId;
    private String leaderId;
    private String username;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvStart;
    private TextView tvEnd;
    private Button btnEvents;
    private Button btnAddEvent;
    private Button btnApproveOrganizers;

    private LeaderFestivalController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_fest_profile);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("id");
        leaderId = intent.getStringExtra("leaderId");
        username = intent.getStringExtra("username");

        tvName = findViewById(R.id.festName);
        tvDesc = findViewById(R.id.desc);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);
        btnEvents = findViewById(R.id.events);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnApproveOrganizers = findViewById(R.id.btn_approve_organizers);

        controller = new LeaderFestivalController(this, accessToken, festivalId, refreshToken);

        controller.getFestivalData();

        btnAddEvent.setOnClickListener(v -> {
            switchActivity(CreateEventActivity.class);
        });
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("id", festivalId);
        intent.putExtra("leaderId", leaderId);
        this.startActivity(intent);
    }

    public void fillInActivity(Festival festival) {
        tvName.setText(festival.getName());
        tvDesc.setText(festival.getDesc());
        tvStart.setText(parseDateTime(festival.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvEnd.setText(parseDateTime(festival.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    public void approveOrganizers(View view) {
        Intent intent = new Intent(this, ApproveOrganizersActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("festival_id", festivalId);
        this.startActivity(intent);
    }

    public void getEvents(View view) {
        Intent intent = new Intent(this, LeaderMyEventsActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("festival_id", festivalId);
        this.startActivity(intent);
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
