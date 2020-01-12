package com.hfad.organizationofthefestival.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.defaultUser.DefaultUserActivity;
import com.hfad.organizationofthefestival.organizer.ApplyFestActivity;
import com.hfad.organizationofthefestival.organizer.EventsActivity;
import com.hfad.organizationofthefestival.organizer.JobsActivity;
import com.hfad.organizationofthefestival.organizer.OrganizerActivity;
import com.hfad.organizationofthefestival.organizer.OrganizerPrintPassActivity;
import com.hfad.organizationofthefestival.utility.User;

import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String searcherUsername;

    private SearchController searchController;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView lvSearchResults;

    private List<User> userList;

    private int searcherPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        tvSearch = findViewById(R.id.tv_search);
        btnSearch = findViewById(R.id.btn_search);
        lvSearchResults = findViewById(R.id.lv_search);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        searcherUsername = intent.getStringExtra("searcherUsername");
        searcherPermission = intent.getIntExtra("searcherPermission", 0);
        searchController = new SearchController(this, accessToken, refreshToken);

        btnSearch.setOnClickListener(v -> {
            search();
        });

        lvSearchResults.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(SearchActivity.this, DefaultUserActivity.class);

            intent1.putExtra("searcherPermission", searcherPermission);
            intent1.putExtra("searcherUsername", searcherUsername);
            intent1.putExtra("permission", userList.get(position).getRole());
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("username", lvSearchResults.getItemAtPosition(position).toString());
            startActivity(intent1);
        });
    }

    public void search() {
        String searched = tvSearch.getText().toString();
        searchController.search(searched);
    }

    public void fillResults(List<User> users) {
        userList = users;
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users.stream().map(User::getUsername).collect(Collectors.toList()));
        lvSearchResults.setAdapter(specializationArrayAdapter);

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
        if (id == R.id.myProfile) {
            switchActivity(OrganizerActivity.class);
        } else if (id == R.id.applyForFest) {
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            switchActivity(EventsActivity.class);
        } else if (id == R.id.myJobs) {
            switchActivity(JobsActivity.class);
        } else if (id == R.id.printPass) {
            Intent intent = new Intent(this, OrganizerPrintPassActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            startActivity(intent);
        } else if (id == R.id.search) {
            finish();
            startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", searcherUsername);
        this.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
