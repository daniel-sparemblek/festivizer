package com.hfad.organizationofthefestival.organizer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.OrganizerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganizerActivity extends AppCompatActivity {

    private ListView festivalList;
    private static OrganizerAdapter adapter;
    private HashMap<String, Integer> festivals;
    private String username;
    private ArrayList<String> namesOfFestivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obsolete_activity_organizer);
        //username = getIntent().getStringExtra("USERNAME");
        //festivalList = findViewById(R.id.festivalList);
        //OrganizerConnector.getFestivals(username, OrganizerActivity.this);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.organizer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            System.out.println("Stisnuo sam My Profile");
        } else if(id == R.id.applyForFest) {
            System.out.println("Stisnuo sam Apply For a Festival");
        }

        return super.onOptionsItemSelected(item);
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
            //OrganizerConnector.applyForFestival(festivalName, username, "Apply", OrganizerActivity.this);
        } else {
            applyBtn.setText("Apply");
            //OrganizerConnector.applyForFestival(festivalName, username, "Cancel", OrganizerActivity.this);
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
}
