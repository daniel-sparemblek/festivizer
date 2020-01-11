package com.hfad.organizationofthefestival.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.NewJob;
import com.hfad.organizationofthefestival.utility.Specialization;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
    private TextView tvStartTime;
    private TextView tvStartDate;

    private Button btnStartDate;
    private Button btnStartTime;
    private Button btnCrateJob;

    private Spinner spFirstSpecialization;
    private Spinner spSecondSpecialization;
    private Spinner spThirdSpecialization;

    private List<String> specsNames;

    private NewJobController controller;

    private int sYear, sMonth, sDay, sHour, sMinute;

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

        btnCrateJob.setOnClickListener(v -> {
            if (checkChosenSpecs()) {
                Toast.makeText(NewJobActivity.this, "Choose different specs.", Toast.LENGTH_SHORT).show();
                return;
            }
            NewJob newJob = new NewJob(etName.getText().toString(),
                    etDescription.getText().toString(),
                    convertTime(tvStartTime.getText().toString(), tvStartDate.getText().toString()),
                    eventId,
                    spFirstSpecialization.getSelectedItem().toString(),
                    spSecondSpecialization.getSelectedItem().toString(),
                    spThirdSpecialization.getSelectedItem().toString());
            controller.createNewJob(newJob);

            returnToEventProfile();
        });

        btnStartDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(NewJobActivity.this,
                    (view, year, month, day) -> showPickedDate(tvStartDate, day, month, year), sYear, sMonth, sDay);
            datePickerDialog.show();
        });

        btnStartTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            sHour = c.get(Calendar.HOUR_OF_DAY);
            sMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(NewJobActivity.this,
                    (view, hour, minute) -> showPickedTime(tvStartTime, hour, minute), sHour, sMinute, true);
            timePickerDialog.show();
        });
    }

    private void returnToEventProfile() {
        Intent intent = new Intent(NewJobActivity.this, ViewEventActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("event_id", eventId);
        intent.putExtra("festivalName", festivalName);
        startActivity(intent);
    }

    private void findViews() {
        tvFestivalName = findViewById(R.id.festName);
        tvEventName = findViewById(R.id.eventName);
        etName = findViewById(R.id.jobName);
        etDescription = findViewById(R.id.jobDesc);
        tvStartTime = findViewById(R.id.startTime);
        tvStartDate = findViewById(R.id.startDate);
        spFirstSpecialization = findViewById(R.id.spec1);
        spSecondSpecialization = findViewById(R.id.spec2);
        spThirdSpecialization = findViewById(R.id.spec3);
        btnCrateJob = findViewById(R.id.org_createJob);
        btnStartTime = findViewById(R.id.startTimebtn);
        btnStartDate = findViewById(R.id.startDatebtn);
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

    private void showPickedDate(TextView et, int day, int month, int year) {
        month++;
        if (day < 10 && month < 10) {
            et.setText("0" + day + "." + "0" + month + "." + year + ".");
        } else if (day < 10) {
            et.setText("0" + day + "." + month + "." + year + ".");
        } else if (month < 10) {
            et.setText(day + "." + "0" + month + "." + year + ".");
        } else {
            et.setText(day + "." + month + "." + year + ".");
        }
    }

    private void showPickedTime(TextView et, int hour, int minutes) {
        if (hour < 10 && minutes < 10) {
            et.setText("0" + hour + ":" + "0" + minutes);
        } else if (hour < 10) {
            et.setText("0" + hour + ":" + minutes);
        } else if (minutes < 10) {
            et.setText(hour + ":" + "0" + minutes);
        } else {
            et.setText(hour + ":" + minutes);
        }
    }

    private String convertTime(String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ZonedDateTime dateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return dateTime.format(dateTimeFormatter) + "T" + time + ":00.000+0000";
    }
}
