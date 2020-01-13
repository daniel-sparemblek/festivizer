package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.FragmentAdapters.EventAdapter;
import com.hfad.organizationofthefestival.search.OrganizerSearchActivity;
import com.hfad.organizationofthefestival.utility.EventApply;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventsActivity extends ApplyFestActivity {

    private EventsController eventsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvEvents;
    EventApply[] gotEvents;

    List<EventApply> completedEvents;
    List<EventApply> activeEvents;

    ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        eventsController.fetchActiveEvents();


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    eventsController.fetchActiveEvents();
                }
                if (position == 1) {
                    eventsController.fetchCompletedEvents();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.organizer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            switchActivity(OrganizerActivity.class);
        } else if (id == R.id.applyForFest) {
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            // do nothing
        } else if (id == R.id.myJobs) {
            switchActivity(JobsActivity.class);
        } else if (id == R.id.printPass) {
            switchActivity(OrganizerPrintPassActivity.class);
        } else if (id == R.id.search) {
            switchActivity(OrganizerSearchActivity.class);
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

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(EventsActivity.this, ViewEventActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("event_id", gotEvents[position].getEventId());
            intent.putExtra("festivalName", gotEvents[position].getFestival().getName());
            startActivity(intent);
        });

        activeEvents = Arrays.stream(events)
                .filter(t -> parseDateTime(t.getEndTime()).isAfter(ZonedDateTime.now()))
                .collect(Collectors.toList());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsToStrings(activeEvents));
        lvEvents.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
    }

    public void fillInCompletedEvents(EventApply[] events) {
        lvEvents = findViewById(R.id.orgCompletedEventList);

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(EventsActivity.this, ViewEventActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("event_id", gotEvents[position].getEventId());
            EventsActivity.this.startActivity(intent);
        });

        completedEvents = Arrays.stream(events)
                .filter(t -> parseDateTime(t.getEndTime()).isBefore(ZonedDateTime.now()))
                .collect(Collectors.toList());

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsToStrings(completedEvents));
        lvEvents.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
    }


    public void saveEvents(EventApply[] events) {
        gotEvents = events;
        dialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }

    private List<String> eventsToStrings(List<EventApply> events) {
        return events.stream()
                .map(EventApply::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
