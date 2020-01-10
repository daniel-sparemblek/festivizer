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

import java.util.LinkedList;

public class EventsActivity extends ApplyFestActivity {

    private EventsController eventsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvEvents;
    private ListView thisIsATest;
    EventApply[] gotEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_my_events); //Using the same layout as jobs

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);

        EventAdapter eventAdapter = new EventAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(eventAdapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabs = findViewById(R.id.event_tabs);
        tabs.setupWithViewPager(viewPager);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        eventsController = new EventsController(this, accessToken, username, refreshToken);

        eventsController.fetchActiveEvents();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 0) {

                }
                if(position == 1) {
                    if(gotEvents == null) {
                        eventsController.fetchCompletedEvents();
                    } else {
                        fillInCompletedEvents(gotEvents);
                    }
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

    public void fillInActiveEvents(EventApply[] events) {
        lvEvents = findViewById(R.id.orgActiveEventList);

        gotEvents = events;

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(EventsActivity.this, ViewEventActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("event_id", gotEvents[position].getEventId());
            intent.putExtra("festivalName", gotEvents[position].getFestival().getName());
            startActivity(intent);
        });

        LinkedList<EventApply> newList = new LinkedList<>();

        for(EventApply event : events) {
            System.out.println(event.getEndTime());
            if(event.getEndTime() == "b") //je li veci
                newList.add(event);
        }

        EventApply[] filteredArray = new EventApply[newList.size()];
        filteredArray = newList.toArray(filteredArray);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsController.format(events));
        lvEvents.setAdapter(specializationArrayAdapter);
    }

    public void fillInCompletedEvents(EventApply[] events) {
        lvEvents = findViewById(R.id.orgCompletedEventList);

        //bartol
        gotEvents = events;

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(EventsActivity.this, ViewEventActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("event_id", gotEvents[position].getEventId());
            EventsActivity.this.startActivity(intent);
        });


        LinkedList<EventApply> newList = new LinkedList<>();

        for(EventApply event : events) {
            if(event.getEndTime() == "b") //je li manji
                newList.add(event);
        }

        EventApply[] filteredArray = new EventApply[newList.size()];
        filteredArray = newList.toArray(filteredArray);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsController.format(events));
        lvEvents.setAdapter(specializationArrayAdapter);

    }


    public void saveEvents(EventApply[] events) {
        gotEvents = events;
    }

}
