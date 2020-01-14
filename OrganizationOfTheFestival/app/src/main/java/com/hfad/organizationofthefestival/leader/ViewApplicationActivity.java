package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

public class ViewApplicationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_applic_profile);

        Gson gson = new Gson();

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Job application information");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ApplicationResponse applicationResponse = gson.fromJson(intent.getStringExtra("application"), ApplicationResponse.class);

        TextView tvWorker = findViewById(R.id.org_worker);
        TextView tvPrice = findViewById(R.id.org_price);
        TextView tvPeople = findViewById(R.id.org_numbOfPeople);
        TextView tvDuration = findViewById(R.id.org_duration);
        TextView tvComment = findViewById(R.id.org_comment);

        tvWorker.setText(applicationResponse.getWorker().getUsername());
        tvPrice.setText(String.valueOf(applicationResponse.getPrice()));
        tvPeople.setText(String.valueOf(applicationResponse.getPeopleNumber()));
        tvDuration.setText(String.valueOf(applicationResponse.getDuration()));
        tvComment.setText(applicationResponse.getComment());
    }
}
