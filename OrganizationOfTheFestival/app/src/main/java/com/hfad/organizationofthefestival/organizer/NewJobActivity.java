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

    private TextView festivalName;
    private TextView eventName;

    private EditText etName;
    private EditText etDescription;
    private EditText etStartTime;

    private Spinner spFirstSpecialization;
    private Spinner spSecondSpecialization;
    private Spinner spThirdSpecialization;

    private NewJobController newJobController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_job_creation);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        eventId = intent.getLongExtra("event_id", 0);

        festivalName = findViewById(R.id.festName);
        eventName = findViewById(R.id.eventName);
        etName = findViewById(R.id.jobName);
        etDescription = findViewById(R.id.jobDesc);
        etStartTime = findViewById(R.id.startTime);
        spFirstSpecialization = findViewById(R.id.spec1);
        spSecondSpecialization = findViewById(R.id.spec2);
        spThirdSpecialization = findViewById(R.id.spec3);
        newJobController = new NewJobController(this, accessToken, username, refreshToken);
        newJobController.getEvent((int)eventId);


    }


    public void fillInActivity(EventApply body) {
        festivalName.setText(body.getFestival().getName());
        eventName.setText(body.getName());
    }
}
