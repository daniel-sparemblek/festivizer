package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.event.creation.CreateEventActivity;
import com.hfad.organizationofthefestival.festival.Festival;

public class LeaderFestivalActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String festivalId;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvStart;
    private TextView tvEnd;
    private Button btnEvents;
    private Button btnAddEvent;
    private Button btnApproveOrganizers;

    private LeaderFestivalController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_fest_profile);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("id");

        tvName = findViewById(R.id.festName);
        tvDesc = findViewById(R.id.desc);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);
        btnEvents = findViewById(R.id.events);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnApproveOrganizers = findViewById(R.id.btn_approve_organizers);

        controller = new LeaderFestivalController(this, accessToken, festivalId, refreshToken);

        controller.getFestivalData();

        btnAddEvent.setOnClickListener(v -> {
            switchActivity(CreateEventActivity.class);
        });
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        this.startActivity(intent);
    }

    public void fillInActivity(Festival festival) {
        tvName.setText(festival.getName());
        tvDesc.setText(festival.getDesc());
        tvStart.setText(festival.getStartTime());
        tvEnd.setText(festival.getEndTime());
    }

    public void approveOrganizers(View view) {
        Intent intent = new Intent(this, ApproveOrganizersActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        System.out.println("FESTIVAL_ID: " + festivalId);
        intent.putExtra("festival_id", festivalId);
        this.startActivity(intent);
    }
}
