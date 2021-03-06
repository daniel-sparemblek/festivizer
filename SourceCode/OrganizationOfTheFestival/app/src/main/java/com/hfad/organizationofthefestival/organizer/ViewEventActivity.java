package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.OrderAdapter;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Job;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewEventActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvFestivalName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvLocation;
    private TextView tvDesc;
    private Button createJob;
    private Button btnUpdateOrder;
    private ListView lvJobs;
    private OrderAdapter savedAdapter;

    private String accessToken;
    private String refreshToken;
    private String username;
    private long eventId;
    private String festivalName;
    private EventApply event;

    private ViewEventController viewEventController;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_event);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Event");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        eventId = intent.getLongExtra("event_id", 0);
        festivalName = intent.getStringExtra("festivalName");

        tvName = findViewById(R.id.org_event_name);
        tvFestivalName = findViewById(R.id.org_festival);
        tvStartTime = findViewById(R.id.org_startTime);
        tvEndTime = findViewById(R.id.org_endTime);
        tvLocation = findViewById(R.id.org_location);
        tvDesc = findViewById(R.id.org_desc);
        createJob = findViewById(R.id.org_newJob);
        btnUpdateOrder = findViewById(R.id.btn_order_update);

        viewEventController = new ViewEventController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        viewEventController.getEvent((int) eventId);
    }

    public void fillInActivity(EventApply event) {
        this.event = event;
        tvName.setText(event.getName());
        tvFestivalName.setText(String.valueOf(event.getFestival().getName()));
        tvDesc.setText(event.getDesc());
        tvLocation.setText(event.getLocation());
        tvStartTime.setText(parseDateTime(event.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvEndTime.setText(parseDateTime(event.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));


        viewEventController.getJobs((int) eventId);

    }

    public void fillInOrderList(Job[] jobs) {

        List<Job> jobsList = new LinkedList<>();

        for(Job job : jobs) {
            jobsList.add(job);
        }

        lvJobs = findViewById(R.id.jobsOrderList);

        OrderAdapter myCustomAdapter = new OrderAdapter(this,
                R.layout.organizer_order_row_layout, jobsList, viewEventController);
        lvJobs.setAdapter(myCustomAdapter);
        savedAdapter = myCustomAdapter;


        dialog.dismiss();
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

    public void createNewJob(View view) {
        Intent intent = new Intent(this, NewJobActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("event_id", eventId);
        intent.putExtra("festivalName", festivalName);
        intent.putExtra("eventName", event.getName());
        this.startActivity(intent);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public List<TextView> getAllViews(ListView listView) {
        List<TextView> views = new ArrayList<>();
        for (int i = 0; i < listView.getChildCount(); i++) {
            views.add((TextView)((LinearLayout)getViewByPosition(i, listView)).getChildAt(0));
        }
        return views;
    }

    public void updateOrder(View view) {
        List<TextView> tvOrderNumbers = getAllViews(this.lvJobs);

        List<String> orderNumbers = tvOrderNumbers.stream()
                .map(t -> t.getText().toString())
                .collect(Collectors.toList());

        Set<String> testSet = new HashSet<>();
        for(String x : orderNumbers) {
            testSet.add(x);
        }

        if(testSet.size() != orderNumbers.size())
            Toast.makeText(this, "Order number cannot be the same!", Toast.LENGTH_SHORT).show();
        else {
            HashMap<String, String> body = new HashMap<>();

            int i = 1;

            for(String orderNumber : orderNumbers) {
                body.put(String.valueOf(i), orderNumber);
                i++;
            }

            viewEventController.updateJobOrders((int)eventId, body);
        }
    }
}
