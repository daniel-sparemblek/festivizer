package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.creation.CreateFestivalActivity;
import com.hfad.organizationofthefestival.search.LeaderSearchActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFestivalsActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String leaderId;
    private String username;
    private Festival[] festivals;

    private MyFestivalsController controller;

    private ListView lvFestivals;
    private Button btnActive;
    private Button btnPending;
    private Button btnCompleted;

    private ProgressDialog dialog;
    private int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_my_fests);

        Toolbar toolbar = findViewById(R.id.leader_toolbar);
        toolbar.setTitle("My festivals");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        leaderId = intent.getStringExtra("leader_id");
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", 1);


        btnActive = findViewById(R.id.btnActive);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);

        lvFestivals = findViewById(R.id.festivalList);

        controller = new MyFestivalsController(this, accessToken, leaderId, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getFestivals("active");
        btnActive.setEnabled(false);


        btnPending.setOnClickListener(v -> {
            controller.getFestivals("pending");
            btnPending.setEnabled(false);
            btnActive.setEnabled(true);
            btnCompleted.setEnabled(true);
        });

        btnActive.setOnClickListener(v -> {
            controller.getFestivals("active");
            btnPending.setEnabled(true);
            btnActive.setEnabled(false);
            btnCompleted.setEnabled(true);
        });

        btnCompleted.setOnClickListener(v -> {
            controller.getFestivals("complete");
            btnPending.setEnabled(true);
            btnActive.setEnabled(true);
            btnCompleted.setEnabled(false);
        });


        lvFestivals.setOnItemClickListener(((parent, view, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);

            for (Festival festival : festivals) {
                if (name.equals(festival.getName())) {
                    switchActivity(LeaderFestivalActivity.class, festival.getFestivalId());
                }
            }
        }));

    }

    private void switchActivity(Class<?> destination, Long id) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("id", id.toString());
        intent.putExtra("leaderId", leaderId);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }

    public void fillInActivity(Festival[] festivals) {
        this.festivals = festivals;

        List<Festival> festivalList = Arrays.asList(festivals);
        ArrayAdapter<String> specializationArrayAdapter =
                new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                festivalList.stream()
                        .map(Festival::getName)
                        .collect(Collectors.toList()));

        lvFestivals.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leader_menu, menu);
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
            switchActivity(LeaderActivity.class);
        } else if (id == R.id.myFests) {
            finish();
            startActivity(getIntent());
        } else if (id == R.id.createNewFest) {
            switchActivity(CreateFestivalActivity.class);
        } else if (id == R.id.jobAuctns) {
            switchActivity(LeaderJobAuctionsActivity.class);
        } else if (id == R.id.search) {
            switchActivity(LeaderSearchActivity.class);
        } else if (id == R.id.printPass) {
            switchActivity(MyFestivalsActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("leader_id", leaderId);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("permission", permission);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
