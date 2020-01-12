package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Event.Event;

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
    private String leaderId;

    private LeaderMyEventsController leaderMyEventsController;
    private List<Event> eventList;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_events);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getStringExtra("id");
        festivalId = intent.getStringExtra("festival_id");

        btnActiveEvents = findViewById(R.id.btnActive);
        btnPendingEvents = findViewById(R.id.btnPending);
        btnCompletedEvents = findViewById(R.id.btnCompleted);
        lvEvents = findViewById(R.id.lv_leader_events);

        leaderMyEventsController = new LeaderMyEventsController(this, accessToken, refreshToken);

        btnActiveEvents.setBackgroundColor(Color.WHITE);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        leaderMyEventsController.getActiveEvents(festivalId);

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(LeaderMyEventsActivity.this, ViewEventActivity.class);
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("refreshToken", refreshToken);
            intent1.putExtra("leader_id", leaderId);
            intent1.putExtra("festival_id", festivalId);
            intent1.putExtra("event_id", eventList.get(position).getEventId());
            startActivity(intent1);
        });
    }


    public void fillInActivity(Event[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventsToStrings(body));
        lvEvents.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
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
