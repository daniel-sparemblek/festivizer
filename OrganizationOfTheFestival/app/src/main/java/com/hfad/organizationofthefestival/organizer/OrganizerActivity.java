package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;

public class OrganizerActivity extends AppCompatActivity {

    private OrganizerController organizerController;
    private String accessToken;
    private String refreshToken;
    private String username;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private ListView lvFestivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("WORKER");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_profile);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        organizerController = new OrganizerController(this, accessToken, username, refreshToken);

        organizerController.getOrganizer();
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
        if (id == R.id.myEvents) {
            System.out.println("Stisnuo sam evente");
        } else if(id == R.id.myProfile) {
            System.out.println("Stisnuo sam profil");
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillInActivity(Organizer organizer) {
        tvName = findViewById(R.id.orgName);
        tvPhone = findViewById(R.id.orgPhone);
        tvEmail = findViewById(R.id.orgEmail);

        System.out.println("kurcic " + organizer);

        tvName.setText(organizer.getUsername());
        tvPhone.setText(organizer.getPhone());
        tvEmail.setText(organizer.getEmail());

        lvFestivals = findViewById(R.id.orgFestivalList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, organizer.getFestivals());
        lvFestivals.setAdapter(specializationArrayAdapter);
    }
}
