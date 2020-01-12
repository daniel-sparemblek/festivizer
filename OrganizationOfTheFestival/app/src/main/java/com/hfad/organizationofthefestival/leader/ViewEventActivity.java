package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    private ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        viewEventController.getEvent((int)eventId);
    }

    public void fillInActivity(EventApply body) {
        tvName.setText(body.getName());
        tvDesc.setText(body.getDesc());
        tvLocation.setText(body.getLocation());
        tvStartTime.setText(parseDateTime(body.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvEndTime.setText(parseDateTime(body.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvOrganizerName.setText(String.valueOf(body.getOrganizer().getUsername()));

        dialog.dismiss();
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
