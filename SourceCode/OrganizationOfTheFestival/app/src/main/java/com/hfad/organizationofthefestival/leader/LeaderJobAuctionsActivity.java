package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.creation.CreateFestivalActivity;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.LeaderSearchActivity;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderJobAuctionsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private int leaderId;
    private LeaderJobAuctionsController controller;

    private ListView jobAuctionsList;
    private ListView jobActiveAuctionsList;

    private List<ApplicationResponse> waitingApplicationList;
    private List<ApplicationResponse> activeApplicationList;


    private ProgressDialog dialog;
    private String username;
    private int permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_waiting_list);

        Toolbar toolbar = findViewById(R.id.leader_toolbar);
        toolbar.setTitle("Job Applications");
        setSupportActionBar(toolbar);

        jobAuctionsList = findViewById(R.id.approvalList1);
        jobActiveAuctionsList = findViewById(R.id.bidList);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getIntExtra("leader_id", 0);
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", 1);

        controller = new LeaderJobAuctionsController(this, accessToken, refreshToken, leaderId);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getJobAuctions();

        jobAuctionsList.setOnItemClickListener((parent, view, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);
            Gson gson = new Gson();

            for(ApplicationResponse applicationResponse : waitingApplicationList) {
                if(name.equals(applicationResponse.getWorker().getUsername())) {
                    String body = gson.toJson(applicationResponse);
                    Intent data = new Intent(this, ViewApplicationActivity.class);
                    data.putExtra("application", body);
                    startActivity(data);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leader_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myProfile) {
            switchActivity(LeaderActivity.class);
        } else if (id == R.id.myFests) {
            switchActivity(MyFestivalsActivity.class);
        } else if (id == R.id.createNewFest) {
            switchActivity(CreateFestivalActivity.class);
        } else if (id == R.id.jobAuctns) {
            finish();
            startActivity(getIntent());
        } else if (id == R.id.search) {
            switchActivity(LeaderSearchActivity.class);
        } else if (id == R.id.printPass) {
            switchActivity(LeaderPrintPassActivity.class);
        } else if (id == R.id.logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("leader_id", leaderId);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("permission", permission);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void fillInWaitingApplications(ApplicationResponse[] applications){
        waitingApplicationList = Arrays.asList(applications);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, format(applications));
        jobAuctionsList.setAdapter(arrayAdapter);
    }

    public void fillInActiveApplications(ApplicationResponse[] applications) {
        activeApplicationList = Arrays.asList(applications);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, format(applications, true));
        jobActiveAuctionsList.setAdapter(arrayAdapter);

        dialog.dismiss();
    }

    private List<String> format(ApplicationResponse[] applicationResponses) {
        return Arrays.stream(applicationResponses)
                .map(t -> t.getWorker().getUsername())
                .collect(Collectors.toList());
    }

    private List<String> format(ApplicationResponse[] applicationResponses, boolean isActive) {
        return Arrays.stream(applicationResponses)
                .map(t -> t.getWorker().getUsername() + " | " + t.getPrice())
                .collect(Collectors.toList());
    }
}
