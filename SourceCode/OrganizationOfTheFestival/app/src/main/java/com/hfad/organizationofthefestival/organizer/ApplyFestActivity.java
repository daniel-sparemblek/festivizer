package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.FestivalsResponse;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.OrganizerSearchActivity;

public class ApplyFestActivity extends AppCompatActivity {

    private ApplyFestController applyFestController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvEvents;
    private String name;
    private char c = 0x2714;
    private FestivalsResponse[] festivals;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_apply_for_festival);

        lvEvents = findViewById(R.id.orgFestivalList);
        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Apply for a festival");
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        applyFestController = new ApplyFestController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        applyFestController.fetchFestivals();

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            name = (String) parent.getItemAtPosition(position);
            int status = 1;

            if(name.contains(" " + c)) {
                name = name.replace(" " + c, "");
                status = 0;
            }

            for(int i = 0; i < festivals.length; i++) {
                if(name.equals(festivals[i].getName())) {
                    festivals[i].setOrgStatus(status);

                    if(status == 1) {
                        applyFestController.apply(festivals[i].getFestivalId());
                    } else {
                        applyFestController.revoke(festivals[i].getFestivalId());

                    }

                    break;
                }
            }

            fillInActivity(festivals);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.organizer_menu, menu);
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
            switchActivity(OrganizerActivity.class);
        } else if (id == R.id.applyForFest) {
            // do nothing
        } else if (id == R.id.myEvents) {
            switchActivity(EventsActivity.class);
        } else if (id == R.id.myJobs) {
            switchActivity(JobsActivity.class);
        } else if (id == R.id.printPass) {
            switchActivity(OrganizerPrintPassActivity.class);
        } else if (id == R.id.search) {
            switchActivity(OrganizerSearchActivity.class);
        } else if (id == R.id.logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }


    public void fillInActivity(FestivalsResponse[] festivals) {
        this.festivals = festivals;

        ArrayAdapter<String> festivalsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, applyFestController.formatFestivals(festivals));
        lvEvents.setAdapter(festivalsArrayAdapter);

        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
