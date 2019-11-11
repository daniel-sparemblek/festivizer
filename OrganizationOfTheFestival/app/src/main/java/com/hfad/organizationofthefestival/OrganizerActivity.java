package com.hfad.organizationofthefestival;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.adapters.OrganizerAdapter;

import java.util.ArrayList;

public class OrganizerActivity extends AppCompatActivity {

    private ArrayList<Festival> festivals;
    private ListView listView;
    private static OrganizerAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);

        button = findViewById(R.id.btnApply);
        listView = findViewById(R.id.festivalList);

        festivals = new ArrayList<>();
        Festival festival = new Festival(444, 777, "jebanje", "bas jako jebanje", 7777777, true);
        festivals.add(festival);
        // get festivals from database

        adapter = new OrganizerAdapter(festivals, getApplicationContext());

        listView.setAdapter((adapter));
    }

    public void onClickApply(View view) {
        Context context;
        Toast toast;
        context = getApplicationContext();
        CharSequence message = "Bravo pedercino";
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
