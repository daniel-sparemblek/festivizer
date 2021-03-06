package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.WaitingListAdapter;
import com.hfad.organizationofthefestival.leader.ViewApplicationActivity;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WaitingListActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String jobId;
    private ListView lvWorkers;
    private ListView lvBids;
    private String fixer;

    private WaitingListController controller;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_waiting_list);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Waiting list");
        setSupportActionBar(toolbar);

        lvWorkers = findViewById(R.id.org_approvalList1);
        lvBids = findViewById(R.id.org_bidsList);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        jobId = intent.getStringExtra("jobId");

        controller = new WaitingListController(this, accessToken, username, refreshToken, Integer.parseInt(jobId));

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getWaitingApplications();
    }

    public void fillInWaiting(ApplicationResponse[] applicationResponses) {
        List<ApplicationResponse> content = Arrays.asList(applicationResponses);

        WaitingListAdapter myCustomAdapter = new WaitingListAdapter(this,
                R.layout.organizer_screen_waiting_list_row, content, controller);
        lvWorkers.setAdapter(myCustomAdapter);
    }

    private List<String> format(List<ApplicationResponse> approved) {
        return approved.stream()
                .map(t -> t.getWorker().getUsername() + " " +  t.getPrice())
                .collect(Collectors.toList());
    }

    public void fillInAccepted(ApplicationResponse[] applicationResponses) {
        List<ApplicationResponse> content = Arrays.asList(applicationResponses);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, format(content));
        lvBids.setAdapter(arrayAdapter);

        dialog.dismiss();

    }

    public void waitingSwitch(ApplicationResponse applicationResponse) {
        Gson gson = new Gson();
        String body = gson.toJson(applicationResponse);
        Intent data = new Intent(this, ViewApplicationActivity.class);
        data.putExtra("application", body);
        startActivity(data);
    }

}
