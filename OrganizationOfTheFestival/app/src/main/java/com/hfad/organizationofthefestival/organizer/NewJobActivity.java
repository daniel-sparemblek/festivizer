package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Specialization;

import java.util.List;
import java.util.stream.Collectors;

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

    private Button createJob;

    private Spinner spFirstSpecialization;
    private Spinner spSecondSpecialization;
    private Spinner spThirdSpecialization;

    private List<String> specsNames;

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
        controller.getSpecializations();

        createJob.setOnClickListener(v -> {
            if (checkChosenSpecs()) {
                Toast.makeText(NewJobActivity.this, "Choose different specs.", Toast.LENGTH_SHORT).show();
                return;
            }
            return;
            // call api
        });
    }


    public void fillInActivity(EventApply body) {

    }

    private void findViews() {
        tvFestivalName = findViewById(R.id.festName);
        tvEventName = findViewById(R.id.eventName);
        etName = findViewById(R.id.jobName);
        etDescription = findViewById(R.id.jobDesc);
        etStartTime = findViewById(R.id.startTime);
        spFirstSpecialization = findViewById(R.id.spec1);
        spSecondSpecialization = findViewById(R.id.spec2);
        spThirdSpecialization = findViewById(R.id.spec3);
        createJob = findViewById(R.id.org_createJob);
    }

    public void fillInFirstSpinner(List<Specialization> specs) {
        specsNames = specs.stream()
                .map(Specialization::getName)
                .collect(Collectors.toList());
        specsNames.add(0, "None");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, specsNames);
        spFirstSpecialization.setAdapter(arrayAdapter);
        spSecondSpecialization.setAdapter(arrayAdapter);
        spThirdSpecialization.setAdapter(arrayAdapter);
    }

    private boolean checkChosenSpecs() {
        if (checkIfSpecsAreNone()){
            Toast.makeText(NewJobActivity.this, "At least one specialization must be chosen.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spFirstSpecialization.getSelectedItem().toString()
                .equals(spSecondSpecialization.getSelectedItem().toString())
                || spFirstSpecialization.getSelectedItem().toString()
                .equals(spThirdSpecialization.getSelectedItem().toString())
                || spThirdSpecialization.getSelectedItem().toString()
                .equals(spSecondSpecialization.getSelectedItem().toString()) && checkIfAnyPairIsNone()) {
            return true;
        }
        return false;
    }

    private boolean checkIfSpecsAreNone() {
        if ("None".equals(spFirstSpecialization.getSelectedItem().toString())
                && "None".equals(spThirdSpecialization.getSelectedItem().toString())
                && "None".equals(spSecondSpecialization.getSelectedItem().toString())) {
            return true;
        }
        return false;
    }

    private boolean checkIfAnyPairIsNone(){
        if ("None".equals(spFirstSpecialization.getSelectedItem().toString())
                && "None".equals(spThirdSpecialization.getSelectedItem().toString())){
            return false;
        } if ("None".equals(spThirdSpecialization.getSelectedItem().toString())
                && "None".equals(spSecondSpecialization.getSelectedItem().toString())) {
            return false;
        } if ("None".equals(spFirstSpecialization.getSelectedItem().toString())
                && "None".equals(spSecondSpecialization.getSelectedItem().toString())) {
            return false;
        }
        return true;
    }
}
