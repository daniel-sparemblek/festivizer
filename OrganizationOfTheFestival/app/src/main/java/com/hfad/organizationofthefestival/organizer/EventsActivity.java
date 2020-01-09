package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.FragmentAdapters.EventAdapter;
import com.hfad.organizationofthefestival.search.SearchActivity;
import com.hfad.organizationofthefestival.utility.EventApply;

public class EventsActivity extends ApplyFestActivity {

    private EventsController eventsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvEvents;
    private ListView thisIsATest;
    EventApply[] eventApplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_my_jobs); //Using the same layout as jobs

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);

        EventAdapter eventAdapter = new EventAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(eventAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        eventsController = new EventsController(this, accessToken, username, refreshToken);

        eventsController.fetchEvents();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    //eventsController.fetchEvents();
                    System.out.println("I ovdje sam uhvatio 1.");
                }
                if(position == 1) {
                    System.out.println("pocinjem ispis:");
                    for(EventApply e : eventApplies) {
                        System.out.println(e.getName());
                    }

                    thisIsATest = findViewById(R.id.orgJobsList); //Using the same layout as jobs
                    ArrayAdapter<String> testAdapter = new ArrayAdapter<>(EventsActivity.this,
                            android.R.layout.simple_list_item_1, eventsController.format(eventApplies));
                    System.out.println(thisIsATest);
                    System.out.println(testAdapter);
                    thisIsATest.setAdapter(testAdapter);
                    System.out.println("I ovdje sam uhvatio 2.");
                }
            }
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
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            // do nothing
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

    public void fillInActivity(EventApply[] events) {
        lvEvents = findViewById(R.id.orgJobsList); //Using the same layout as jobs
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsController.format(events));
        lvEvents.setAdapter(specializationArrayAdapter);

        eventApplies = events;

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(EventsActivity.this, ViewEventActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("event_id", eventApplies[position].getEventId());
            EventsActivity.this.startActivity(intent);
        });


    }

//    public void fillInCompletedActivity(EventApply[] events) {
//        for(EventApply event : events) {
//            System.out.println(event);
//        }
//        lvEvents = findViewById(R.id.orgEventList); //Using the same layout as jobs
//        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, eventsController.format(events));
//        lvEvents.setAdapter(specializationArrayAdapter);
//
//    }

}
