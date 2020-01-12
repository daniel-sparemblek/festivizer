package com.hfad.organizationofthefestival.festival.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CreateFestivalActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FestivalCreationController controller;
    private String accessToken;
    private String refreshToken;
    private String username;

    private ImageView ivLogo;
    private EditText etName;
    private EditText etDescription;
    private TextView etStartTime;
    private TextView etStartDate;
    private TextView etEndTime;
    private TextView etEndDate;
    private Button btCreate;

    private Festival festival;


    private Button btnStartDatePicker, btnStartTimePicker;
    private Button btnEndDatePicker, btnEndTimePicker;


    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_festival_creation);

        findViews();
        controller = new FestivalCreationController(this);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        btnStartDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(CreateFestivalActivity.this,
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
            datePickerDialog = new DatePickerDialog(CreateFestivalActivity.this, (view, year, month, day)
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

        ivLogo.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            CreateFestivalActivity.this.startActivityForResult(intent1, PICK_IMAGE_REQUEST);
        });

        btCreate.setOnClickListener(v -> {
            if (!checkEntry()) {
                return;
            }

            String startDateTime = convertTime(etStartTime.getText().toString(), etStartDate.getText().toString());
            String endDateTime = convertTime(etEndTime.getText().toString(), etEndDate.getText().toString());

            if (parseDateTime(startDateTime).isBefore(parseDateTime(endDateTime)) && parseDateTime(startDateTime).isAfter(ZonedDateTime.now())){
                festival = new Festival(etName.getText().toString(),
                        etDescription.getText().toString(),
                        getPictureString(),
                        startDateTime,
                        endDateTime);
                controller.createFestival(festival, accessToken);

                returnToLeaderActivity();
            } else {
                Toast.makeText(this, "Start date should be before end date or after today's date.", Toast.LENGTH_SHORT).show();
            }


        });
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
        Intent intent = new Intent(CreateFestivalActivity.this, LeaderActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void findViews() {
        ivLogo = findViewById(R.id.festivalLogo);
        etName = findViewById(R.id.name);
        etDescription = findViewById(R.id.desc);
        btCreate = findViewById(R.id.createBtn);

        btnStartDatePicker = findViewById(R.id.startDatebtn);
        btnStartTimePicker = findViewById(R.id.startTimebtn);
        etStartDate = findViewById(R.id.startDate);
        etStartTime = findViewById(R.id.startTime);

        btnEndDatePicker = findViewById(R.id.endDatebtn);
        btnEndTimePicker = findViewById(R.id.endTimebtn);
        etEndDate = findViewById(R.id.endDate);
        etEndTime = findViewById(R.id.endTime);
    }

    private String getPictureString() {
        Bitmap bitmap = ((BitmapDrawable) ivLogo.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] pictureByte = bos.toByteArray();
        return Base64.encodeToString(pictureByte, Base64.DEFAULT);
    }

    private boolean checkEntry() {
        if ("".equals(etName.getText().toString())) {
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etDescription.getText().toString())) {
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
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
    private String convertTime(String time, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ZonedDateTime dateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return dateTime.format(dateTimeFormatter) + "T" + time + ":00.000+0000";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ivLogo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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