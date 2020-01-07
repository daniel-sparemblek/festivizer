package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFestivalsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderId;

    private MyFestivalsController myFestivalsController;

    private ListView lvFestivals;
    private Button btnActive;
    private Button btnPending;
    private Button btnCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_fests);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getStringExtra("leader_id");

        btnActive = findViewById(R.id.btnActive);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);

        lvFestivals = findViewById(R.id.festivalList);

        myFestivalsController = new MyFestivalsController(this, accessToken, leaderId, refreshToken);
        myFestivalsController.getActiveFestivals();

    }

    public void fillInActivity(Festival[] festivals) {
        List<Festival> festivalList = Arrays.asList(festivals);
        ArrayAdapter<String> specializationArrayAdapter =
                new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                festivalList.stream()
                        .map(Festival::getName)
                        .collect(Collectors.toList()));
        btnActive.setEnabled(false);

        lvFestivals.setAdapter(specializationArrayAdapter);
    }
}
