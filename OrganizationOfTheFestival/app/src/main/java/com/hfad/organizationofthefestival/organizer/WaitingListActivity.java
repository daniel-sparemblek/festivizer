package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.WaitingListAdapter;
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

        controller.setApplicationStatus();
        controller.getWaitingApplications();
    }

    public void fillInWaiting(ApplicationResponse[] applicationResponses) {
        List<ApplicationResponse> content = Arrays.asList(applicationResponses);

        WaitingListAdapter myCustomAdapter = new WaitingListAdapter(this,
                R.layout.organizer_screen_waiting_list_row, content, controller);
        lvWorkers.setAdapter(myCustomAdapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, format(approved));
        lvBids.setAdapter(arrayAdapter);

        dialog.dismiss();

=======
>>>>>>> e26669cb1b5782e2132f5238492f58f8b764f2d3
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
    }
}
