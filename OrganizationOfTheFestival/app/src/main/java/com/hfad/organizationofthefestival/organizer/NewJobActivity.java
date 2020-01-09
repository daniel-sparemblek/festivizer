package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;

public class NewJobActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private long eventId;
    private String eventName;
    private String festivalName;

    private TextView tvFestivalName;
    private TextView tvEventName;

    private EditText etName;
    private EditText etDescription;
    private EditText etStartTime;

    private Spinner spFirstSpecialization;
    private Spinner spSecondSpecialization;
    private Spinner spThirdSpecialization;

    private NewJobController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job_creation);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        eventId = intent.getLongExtra("event_id", 0);
        eventName = intent.getStringExtra("eventName");
        festivalName = intent.getStringExtra("festivalName");

        findViews();

        tvEventName.setText(eventName);
        tvFestivalName.setText(festivalName);

        controller = new NewJobController(this, accessToken, username, refreshToken, eventName, festivalName);
        controller.createNewJob();
    }


    public void fillInActivity(EventApply body) {

    }

    private void findViews(){
        tvFestivalName = findViewById(R.id.festName);
        tvEventName = findViewById(R.id.eventName);
        etName = findViewById(R.id.jobName);
        etDescription = findViewById(R.id.jobDesc);
        etStartTime = findViewById(R.id.startTime);
        spFirstSpecialization = findViewById(R.id.spec1);
        spSecondSpecialization = findViewById(R.id.spec2);
        spThirdSpecialization = findViewById(R.id.spec3);
    }
}
