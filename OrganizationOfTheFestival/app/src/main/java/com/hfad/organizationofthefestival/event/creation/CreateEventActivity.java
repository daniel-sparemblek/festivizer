package com.hfad.organizationofthefestival.event.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.event.Event;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

public class CreateEventActivity extends AppCompatActivity {


    private EventCreationController controller;
    private String accessToken;
    private String refreshToken;

    private EditText etName;
    private EditText etDescription;
    private EditText etLocation;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btCreate;

    private Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_create_event);

        findViews();
        controller = new EventCreationController(this);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");

        btCreate.setOnClickListener(v -> {
            if (!CreateEventActivity.this.checkEntry()) {
                return;
            }
            event = new Event(etName.getText().toString(),
                    etDescription.getText().toString(),
                    Integer.parseInt(etLocation.getText().toString()),
                    etStartTime.getText().toString(),
                    etEndTime.getText().toString());
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
        }
        return true;
    }

}
