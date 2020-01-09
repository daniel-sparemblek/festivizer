package com.hfad.organizationofthefestival.festival.Event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.leader.LeaderActivity;
import com.hfad.organizationofthefestival.organizer.Organizer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private EventCreationController controller;
    private String accessToken;
    private String refreshToken;
    private String festivalId;

    private EditText etName;
    private EditText etDescription;
    private EditText etLocation;
    private TextView etStartTime;
    private TextView etStartDate;
    private TextView etEndTime;
    private TextView etEndDate;
    private Button btCreate;

    private Spinner spinner;

    private Event event;

    Organizer[] organizers;

    private Button btnStartDatePicker, btnStartTimePicker;
    private Button btnEndDatePicker, btnEndTimePicker;

    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_create_event);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("id");

        controller = new EventCreationController(this, accessToken, festivalId, refreshToken);

        findViews();
        controller.getOrganizers();

        System.out.println("FFFFFFFF");

        btnStartDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                    (view, year, month, day) -> showPickedDate(etStartDate, day, month, year), sYear, sMonth, sDay);
            datePickerDialog.show();
        });

        btnStartTimePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            sHour = c.get(Calendar.HOUR_OF_DAY);
            sMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(this,
                    (view, hour, minute) -> showPickedTime(etStartTime, hour, minute), sHour, sMinute, true);
            timePickerDialog.show();
        });

        btnEndDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (view, year, month, day)
                    -> showPickedDate(etEndDate, day, month, year), eYear, eMonth, eDay);
            datePickerDialog.show();
        });

        btnEndTimePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            eHour = c.get(Calendar.HOUR_OF_DAY);
            eMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(this,
                    (view, hour, minute) -> showPickedTime(etEndTime, hour, minute), eHour, eMinute, true);
            timePickerDialog.show();
        });

        btCreate.setOnClickListener(v -> {
            if (!checkEntry()) {
                return;
            }

            String startDateTime = convertTime(etStartTime.getText().toString(), etStartDate.getText().toString());
            String endDateTime = convertTime(etEndTime.getText().toString(), etEndDate.getText().toString());

            event = new Event(etName.getText().toString(),
                    etDescription.getText().toString(),
                    etLocation.getText().toString(),
                    startDateTime,
                    endDateTime
                    );
            String aggaerg = intent.getStringExtra("leaderId");
            System.out.println("NEMEREN " + aggaerg);
            event.setLeaderId(Long.parseLong(intent.getStringExtra("leaderId")));
            controller.createEvent(event, accessToken);

            returnToLeaderActivity();
        });
    }

    public void setupSpinner(Organizer[] organizers) {
        this.organizers = organizers;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getOrganizers(organizers));
        spinner.setAdapter(adapter);
    }

    private List<String> getOrganizers(Organizer[] organizers) {
        List<String> result = new ArrayList<>();

        for(Organizer organizer : organizers) {
            result.add(organizer.getUsername());
        }

        return result;
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

    private void returnToLeaderActivity() {
        Intent intent = new Intent(CreateEventActivity.this, LeaderActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("festivalId", festivalId);
        startActivity(intent);
    }

    private void findViews() {
        etLocation = findViewById(R.id.location);
        etName = findViewById(R.id.name);
        etDescription = findViewById(R.id.desc);
        btCreate = findViewById(R.id.createEvent);

        btnStartDatePicker = findViewById(R.id.startDatebtn);
        btnStartTimePicker = findViewById(R.id.startTimebtn);
        etStartDate = findViewById(R.id.startDate);
        etStartTime = findViewById(R.id.startTime);

        btnEndDatePicker = findViewById(R.id.endDatebtn);
        btnEndTimePicker = findViewById(R.id.endTimebtn);
        etEndDate = findViewById(R.id.endDate);
        etEndTime = findViewById(R.id.endTime);

        spinner = findViewById(R.id.selOrganizerIn);
    }

    private boolean checkEntry() {
        if ("".equals(etName.getText().toString())) {
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etDescription.getText().toString())) {
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if("".equals(etLocation.getText().toString())) {
            Toast.makeText(this, "Location can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartTime.getText().toString()) || "".equals(etEndTime.getText().toString())) {
            Toast.makeText(this, "Start and and time must be specified", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartDate.getText().toString()) || "".equals(etEndDate.getText().toString())) {
            Toast.makeText(this, "Start and end date must be specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // this is shit but it works (kinda :))
    public String convertTime(String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ZonedDateTime dateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return dateTime.format(dateTimeFormatter) + "T" + time + ":00.000+0000";
    }
}