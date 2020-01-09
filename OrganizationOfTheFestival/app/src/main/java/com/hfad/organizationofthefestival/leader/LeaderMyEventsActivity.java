package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.event.Event;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderMyEventsActivity extends AppCompatActivity {

    private Button btnActiveEvents;
    private Button btnPendingEvents;
    private Button btnCompletedEvents;
    private ListView lvEvents;

    private String accessToken;
    private String refreshToken;
    private String festivalId;

    private LeaderMyEventsController leaderMyEventsController;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_events);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("id");
        festivalId = intent.getStringExtra("festival_id");

        btnActiveEvents = findViewById(R.id.btnActive);
        btnPendingEvents = findViewById(R.id.btnPending);
        btnCompletedEvents = findViewById(R.id.btnCompleted);
        lvEvents = findViewById(R.id.lv_leader_events);

        leaderMyEventsController = new LeaderMyEventsController(this, accessToken, refreshToken);

        btnActiveEvents.setBackgroundColor(Color.WHITE);
        leaderMyEventsController.getActiveEvents(festivalId);
    }


    public void fillInActivity(Event[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsToStrings(body));
        lvEvents.setAdapter(specializationArrayAdapter);
    }

    public List<String> eventsToStrings(Event[] events) {
        eventList = Arrays.asList(events);

        List<String> stringList = eventList.stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());
        return stringList;
    }

    public void getActiveEvents(View view) {
        btnCompletedEvents.setBackgroundColor(Color.TRANSPARENT);
        btnPendingEvents.setBackgroundColor(Color.TRANSPARENT);
        btnActiveEvents.setBackgroundColor(Color.WHITE);
        leaderMyEventsController.getActiveEvents(festivalId);
    }

    public void getCompletedEvents(View view) {
        btnCompletedEvents.setBackgroundColor(Color.WHITE);
        btnPendingEvents.setBackgroundColor(Color.TRANSPARENT);
        btnActiveEvents.setBackgroundColor(Color.TRANSPARENT);
        leaderMyEventsController.getCompletedEvents(festivalId);
    }

    public void getPendingEvents(View view) {
        btnCompletedEvents.setBackgroundColor(Color.TRANSPARENT);
        btnPendingEvents.setBackgroundColor(Color.WHITE);
        btnActiveEvents.setBackgroundColor(Color.TRANSPARENT);
        leaderMyEventsController.getPendingEvents(festivalId);
    }
}
