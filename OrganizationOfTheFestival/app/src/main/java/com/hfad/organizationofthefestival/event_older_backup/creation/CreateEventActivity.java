package com.hfad.organizationofthefestival.event_older_backup.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.event_older_backup.Event;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener{


    private EventCreationController controller;
    private String accessToken;
    private String refreshToken;

    private EditText etName;
    private EditText etDescription;
    private EditText etLocation;
    private Button btCreate;

    private Event event;


    Button btnStartDatePicker, btnStartTimePicker;
    Button btnEndDatePicker, btnEndTimePicker;

    EditText etStartDate, etStartTime;
    EditText etEndDate, etEndTime;

    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_create_event);

        findViews();
        controller = new EventCreationController(this);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");

        // start time
        btnStartDatePicker =(Button)findViewById(R.id.startDatebtn);
        btnStartTimePicker =(Button)findViewById(R.id.startTimebtn);
        etStartDate=(EditText)findViewById(R.id.startDate);
        etStartTime=(EditText)findViewById(R.id.startTime);

        btnStartDatePicker.setOnClickListener(this);
        btnStartTimePicker.setOnClickListener(this);

        // end time

        btnEndDatePicker =(Button)findViewById(R.id.endDatebtn);
        btnEndTimePicker =(Button)findViewById(R.id.endTimebtn);
        etEndDate=(EditText)findViewById(R.id.endDate);
        etEndTime=(EditText)findViewById(R.id.endTime);

        btnEndDatePicker.setOnClickListener(this);
        btnEndTimePicker.setOnClickListener(this);




        String startDateTime = convertTime(etStartTime.getText().toString(),
                etStartDate.getText().toString());
        String endDateTime = convertTime(etEndTime.getText().toString(),
                etEndDate.getText().toString());

        btCreate.setOnClickListener(v -> {
            if (!CreateEventActivity.this.checkEntry()) {
                return;
            }
            event = new Event(etName.getText().toString(),
                    etDescription.getText().toString(),
                    etLocation.getText().toString(),
                    startDateTime,
                    endDateTime);
            controller.createEvent(event, accessToken);
            CreateEventActivity.this.returnToLeaderActivity();
        });

    }

    private void returnToLeaderActivity() {
        Intent intent = new Intent(CreateEventActivity.this, LeaderActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        etName = findViewById(R.id.name);
        etDescription = findViewById(R.id.desc);
        etLocation = findViewById(R.id.location);
        etStartTime = findViewById(R.id.startTime);
        etEndTime = findViewById(R.id.endTime);
        btCreate = findViewById(R.id.createBtn);
    }

    private boolean checkEntry(){
        if ("".equals(etName.getText().toString())){
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etDescription.getText().toString())){
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etLocation.getText().toString())){
            Toast.makeText(this, "Location can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartTime.getText().toString()) || "".equals(etEndTime.getText().toString())){
            Toast.makeText(this, "Start and end time must be specified", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartDate.getText().toString()) || "".equals(etEndDate.getText().toString())){
            Toast.makeText(this, "Start and end date must be specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
// start time
        if (v == btnStartDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> etStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), sYear, sMonth, sDay);
            datePickerDialog.show();
        }
        if (v == btnStartTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            sHour = c.get(Calendar.HOUR_OF_DAY);
            sMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> etStartTime.setText(hourOfDay + ":" + minute), sHour, sMinute, false);
            timePickerDialog.show();
        }

        // end time
        if (v == btnEndDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            etEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, eYear, eMonth, eDay);
            datePickerDialog.show();
        }
        if (v == btnEndTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            eHour = c.get(Calendar.HOUR_OF_DAY);
            eMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> etEndTime.setText(hourOfDay + ":" + minute), eHour, eMinute, false);
            timePickerDialog.show();
        }
    }


    public String convertTime(String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        ZonedDateTime dateTime = ZonedDateTime.parse(date + " " + time, formatter);
        return dateTime.toString();

    }

}
