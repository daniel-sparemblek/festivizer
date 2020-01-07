package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

        myApplications.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(MyApplicationsActivity.this, ViewApplicationActivity.class);
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("refreshToken", refreshToken);
            intent1.putExtra("username", username);
            intent1.putExtra("job_name", applicationList.get(position).getAuction().getJob().getName());
            intent1.putExtra("app_id", applicationList.get(position).getApplicationId());
            MyApplicationsActivity.this.startActivity(intent1);
        });
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
