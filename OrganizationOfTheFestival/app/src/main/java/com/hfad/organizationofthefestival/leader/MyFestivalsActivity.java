package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.Festivals;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFestivalsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderId;

    private MyFestivalsController myFestivalsController;

    private ListView lvFestivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_fests);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getStringExtra("leader_id");

        lvFestivals = findViewById(R.id.festivalList);

        myFestivalsController = new MyFestivalsController(this, accessToken, leaderId, refreshToken);
        myFestivalsController.getCompletedFestivals();
    }

    public void fillInActivity(Festival[] festivals) {
        List<Festival> festivalList = Arrays.asList(festivals);
        ArrayAdapter<String> specializationArrayAdapter =
                new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                festivalList.stream()
                        .map(Festival::getName)
                        .collect(Collectors.toList()));

        lvFestivals.setAdapter(specializationArrayAdapter);
    }
}
