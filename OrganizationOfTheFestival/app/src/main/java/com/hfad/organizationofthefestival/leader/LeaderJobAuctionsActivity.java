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
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.ApplicationAdapter;
import com.hfad.organizationofthefestival.festival.creation.CreateFestivalActivity;
import com.hfad.organizationofthefestival.search.LeaderSearchActivity;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.Arrays;
import java.util.List;

public class LeaderJobAuctionsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private int leaderId;
    private LeaderJobAuctionsController controller;

    private ListView jobAuctionsList;

    private List<ApplicationResponse> applicationList;

    private ProgressDialog dialog;
    private String username;
    private int permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_job_auc);

        Toolbar toolbar = findViewById(R.id.leader_toolbar);
        toolbar.setTitle("Job applications");
        setSupportActionBar(toolbar);

        jobAuctionsList = findViewById(R.id.jobAuctionList);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getIntExtra("leader_id", 0);
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", 1);

        controller = new LeaderJobAuctionsController(this, accessToken, refreshToken, Integer.toString(leaderId));

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getJobAuctions();
    }

    public void fillInActivity(ApplicationResponse[] applications){
        applicationList = Arrays.asList(applications);

        ApplicationAdapter applicationAdapter = new ApplicationAdapter(this, R.layout.application_row_layout, applicationList);

        jobAuctionsList.setAdapter(applicationAdapter);

        dialog.dismiss();

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
    }
}
