package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFestivalsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderId;
    private String username;
    private Festival[] festivals;

    private MyFestivalsController controller;

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
        username = intent.getStringExtra("username");

        btnActive = findViewById(R.id.btnActive);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);

        lvFestivals = findViewById(R.id.festivalList);

        controller = new MyFestivalsController(this, accessToken, leaderId, refreshToken);
        controller.getFestivals("active");
        btnActive.setEnabled(false);


        btnPending.setOnClickListener(v -> {
            controller.getFestivals("pending");
            btnPending.setEnabled(false);
            btnActive.setEnabled(true);
            btnCompleted.setEnabled(true);
        });

        btnActive.setOnClickListener(v -> {
            controller.getFestivals("active");
            btnPending.setEnabled(true);
            btnActive.setEnabled(false);
            btnCompleted.setEnabled(true);
        });

        btnCompleted.setOnClickListener(v -> {
            controller.getFestivals("complete");
            btnPending.setEnabled(true);
            btnActive.setEnabled(true);
            btnCompleted.setEnabled(false);
        });


        lvFestivals.setOnItemClickListener(((parent, view, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);

            for (Festival festival : festivals) {
                if (name.equals(festival.getName())) {
                    switchActivity(LeaderFestivalActivity.class, festival.getFestivalId());
                }
            }
        }));

    }

    private void switchActivity(Class<?> destination, Long id) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("id", id.toString());
        intent.putExtra("leaderId", leaderId);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }

    public void fillInActivity(Festival[] festivals) {
        this.festivals = festivals;

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
