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

public class OrganizerActivity extends AppCompatActivity implements OrganizerConnector.OrganizerListener {

    private ListView festivalList;
    private static OrganizerAdapter adapter;
    private ArrayList<String> festivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);

        festivalList = findViewById(R.id.festivalList);
        OrganizerConnector.getFestivals(OrganizerActivity.this);
    }

    public void onClickApply(View view) {
        final int position = festivalList.getPositionForView((LinearLayout)view.getParent());
        String name = festivals.get(position);
        LinearLayout l1 = (LinearLayout)view.getParent();
        Button applyBtn = l1.findViewById(R.id.btnApply);
        if(applyBtn.getText().toString().equals("Apply")) {
            applyBtn.setText("Cancel");
            OrganizerConnector.applyForFestival(name, getIntent().getStringExtra("USERNAME"), OrganizerActivity.this);
        } else {
            applyBtn.setText("Apply");
            toast("You canceled your request.");
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
    public void onGetFestivalsResponse(ArrayList<String> festivals){
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
