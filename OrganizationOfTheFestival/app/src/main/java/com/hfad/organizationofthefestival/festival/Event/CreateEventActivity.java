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
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

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
    private String festivalStartDateTime;
    private String festivalEndDateTime;

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
    private String startDateTime;
    private String endDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_create_event);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("id");
        festivalStartDateTime = intent.getStringExtra("startDateTime");
        festivalEndDateTime = intent.getStringExtra("endDateTime");


        System.out.println(festivalEndDateTime);
        System.out.println(festivalStartDateTime);

        controller = new EventCreationController(this, accessToken, festivalId, refreshToken);

        findViews();
        controller.getOrganizers();

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

            String name = etName.getText().toString();
            String desc = etDescription.getText().toString();
            String location = etLocation.getText().toString();
            startDateTime = convertTime(etStartTime.getText().toString(), etStartDate.getText().toString());
            endDateTime = convertTime(etEndTime.getText().toString(), etEndDate.getText().toString());


            int position = spinner.getSelectedItemPosition();

            if(position == -1) {
                Toast.makeText(this, "There is no organizer selected!", Toast.LENGTH_SHORT).show();
            } else {
                if (!parseDateTime(startDateTime).isBefore(parseDateTime(endDateTime))){
                    Toast.makeText(this, "Start time should be before end time.", Toast.LENGTH_SHORT).show();
                } else if (!parseDateTime(startDateTime).isAfter(ZonedDateTime.now())){
                    Toast.makeText(this, "Start time should be after today's time.", Toast.LENGTH_SHORT).show();
                } else if (!parseDateTime(startDateTime).isAfter(parseDateTime(festivalStartDateTime))){
                    Toast.makeText(this, "Start time should be after festivals start time.", Toast.LENGTH_SHORT).show();
                } else if (!parseDateTime(endDateTime).isBefore(parseDateTime(festivalEndDateTime))){
                    Toast.makeText(this, "End time should be before festivals end time.", Toast.LENGTH_SHORT).show();
                } else if (!parseDateTime(startDateTime).isBefore(parseDateTime(festivalEndDateTime))){
                    Toast.makeText(this, "Start time should be before festivals end time.", Toast.LENGTH_SHORT).show();
                } else if (!parseDateTime(endDateTime).isAfter(parseDateTime(festivalStartDateTime))) {
                    Toast.makeText(this, "End time should be before festivals start time.", Toast.LENGTH_SHORT).show();
                } else {
                    Organizer organizer = organizers[position];

                    WorkingEvent event = new WorkingEvent(Integer.parseInt(festivalId), organizer.getId(),
                            name, desc, location, startDateTime, endDateTime);

                    controller.createEvent(event, accessToken);

                    finish();
                }

            }
        });
    }

    public void setupSpinner(Organizer[] organizers) {
        this.organizers = organizers;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getOrganizers(organizers));
        spinner.setAdapter(adapter);
    }

    private List<String> getOrganizers(Organizer[] organizers) {
        List<String> result = new ArrayList<>();

        for (Organizer organizer : organizers) {
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
        } else if ("".equals(etLocation.getText().toString())) {
            Toast.makeText(this, "Location can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartTime.getText().toString()) || "".equals(etEndTime.getText().toString())) {
            Toast.makeText(this, "Start and end time must be specified", Toast.LENGTH_SHORT).show();
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