package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderPrintPassActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private int leaderId;

    private Spinner spFestivalPicker;
    private Button btnGeneratePass;

    private LeaderPrintPassController leaderPrintPassController;

    private List<Festival> festivalList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_print_pass);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        leaderId = intent.getIntExtra("leader_id", 0);

        spFestivalPicker = findViewById(R.id.leader_sp_festival_picker);
        btnGeneratePass = findViewById(R.id.leader_btn_generate_pass);

        leaderPrintPassController = new LeaderPrintPassController(this, accessToken, username, refreshToken);
        leaderPrintPassController.getLeaderFestivals(leaderId);

    }


    public void fillInActivity(Festival[] body) {
        ArrayAdapter<String> festivalsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, festivalsToStrings(body));

        spFestivalPicker.setAdapter(festivalsArrayAdapter);
    }

    public List<String> festivalsToStrings(Festival[] festivals) {
        festivalList = Arrays.asList(festivals);

        return festivalList.stream()
                .map(Festival::getName)
                .collect(Collectors.toList());
    }
}
