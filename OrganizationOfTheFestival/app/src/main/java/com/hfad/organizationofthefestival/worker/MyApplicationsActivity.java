package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Application;
import com.hfad.organizationofthefestival.utility.WorkersApplication;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyApplicationsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    private ListView myApplications;
    private List<WorkersApplication> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_my_applications);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        myApplications = findViewById(R.id.applicationsList);

        MyApplicationsController myApplicationsControler = new MyApplicationsController(this
                , accessToken, username, refreshToken);
        myApplicationsControler.getWorkerApplications();
    }


    public void fillInActivity(WorkersApplication[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, applicationsToStrings(body));
        myApplications.setAdapter(specializationArrayAdapter);
    }

    public List<String> applicationsToStrings(WorkersApplication[] applications) {
        applicationList = Arrays.asList(applications);

        List<String> stringList = applicationList.stream()
                .map(t -> t.getAuction().getJob().getName())
                .collect(Collectors.toList());
        return stringList;
    }
}
