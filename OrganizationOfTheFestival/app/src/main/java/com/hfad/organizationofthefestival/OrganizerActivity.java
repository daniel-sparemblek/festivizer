package com.hfad.organizationofthefestival;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.adapters.OrganizerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganizerActivity extends AppCompatActivity implements OrganizerConnector.OrganizerListener {

    private ListView festivalList;
    private static OrganizerAdapter adapter;
    private HashMap<String, Integer> festivals;
    private String username;
    private ArrayList<String> namesOfFestivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        username = getIntent().getStringExtra("USERNAME");
        festivalList = findViewById(R.id.festivalList);
        OrganizerConnector.getFestivals(username, OrganizerActivity.this);
    }

    public void onClickApply(View view) {
        final int position = festivalList.getPositionForView((LinearLayout)view.getParent());
        namesOfFestivals = new ArrayList<>();
        namesOfFestivals.addAll(festivals.keySet());
        String festivalName = namesOfFestivals.get(position);
        LinearLayout l1 = (LinearLayout)view.getParent();
        Button applyBtn = l1.findViewById(R.id.btnApply);
        if(applyBtn.getText().toString().equals("Apply")) {
            applyBtn.setText("Cancel");
            OrganizerConnector.applyForFestival(festivalName, username, "Apply", OrganizerActivity.this);
        } else {
            applyBtn.setText("Apply");
            OrganizerConnector.applyForFestival(festivalName, username, "Cancel", OrganizerActivity.this);
        }
    }

    private void toast(CharSequence message) {
        Context context;
        Toast toast;
        context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    public void onGetFestivalsResponse(Arr){
        adapter = new OrganizerAdapter(festivals, this);
        this.festivals = festivals;
        festivalList.setAdapter((adapter));
    }

    @Override
    public void onApplyForFestivalResponse(ServerStatus status) {
        if (status == ServerStatus.SUCCESS){
            toast("Great! Your request is being processed.");
        }
        if (status == ServerStatus.SERVER_DOWN){
            toast("Server is down");
        }
        if (status == ServerStatus.UNKNOWN) {
            toast("Something went wrong. Please try again later.");
        }
    }
}
