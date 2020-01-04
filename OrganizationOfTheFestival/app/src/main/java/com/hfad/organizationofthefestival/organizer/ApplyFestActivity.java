package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.util.ArrayList;
import java.util.List;

public class ApplyFestActivity extends AppCompatActivity {

    private ApplyFestController applyFestController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvFestivals;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_apply_for_festival);

        lvFestivals = findViewById(R.id.orgFestivalList);
        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        applyFestController = new ApplyFestController(this, accessToken, username, refreshToken);

        applyFestController.fetchFestivals();

        lvFestivals.setOnItemClickListener((parent, view, position, id) -> name  = (String) parent.getItemAtPosition(position));
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
            switchActivity(PrintPassActivity.class);
        } else if (id == R.id.search) {
            switchActivity(SearchActivity.class);
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


    public void fillInActivity(Festival[] festivals) {
        ArrayAdapter<String> festivalsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, formatFestivals(festivals));
        lvFestivals.setAdapter(festivalsArrayAdapter);
    }

    private List<String> formatFestivals(Festival[] festivals) {
        List<String> result = new ArrayList<>();
        char c = 0x2714;

        for(Festival festival : festivals) {
            if (festival.getStatus() == 1) {
                result.add(festival.toString() + " " + c);
            } else if (festival.getStatus() == 0) {
                result.add(festival.toString());
            }
        }

        return result;
    }
}
