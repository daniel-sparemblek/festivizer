package com.hfad.organizationofthefestival.leader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFestivalsActivity extends AppCompatActivity {

    private String username;
    private String accessToken;
    private String refreshToken;
    private int leaderId;

    private MyFestivalsController myFestivalsController;

    private ListView lvFestivals;
    private List<Festival> festivalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_fests);

        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");
        leaderId = getIntent().getIntExtra("leader_id", 0);

        lvFestivals = findViewById(R.id.festivalList);

        myFestivalsController = new MyFestivalsController(this, accessToken, username, refreshToken);
        myFestivalsController.getCompletedFestivals(leaderId);
    }

    public void fillInActivity(Festival[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, festivalsToStrings(body));
        lvFestivals.setAdapter(specializationArrayAdapter);
    }

    public List<String> festivalsToStrings(Festival[] festivals) {
        festivalsList = Arrays.asList(festivals);

        List<String> stringList = festivalsList.stream()
                .map(Festival::getName)
                .collect(Collectors.toList());
        return stringList;
    }
}
