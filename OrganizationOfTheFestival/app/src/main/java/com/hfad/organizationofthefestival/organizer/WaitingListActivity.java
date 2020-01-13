package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hfad.organizationofthefestival.R;

public class WaitingListActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_waiting_list);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Waiting list");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
    }
}
