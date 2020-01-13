package com.hfad.organizationofthefestival.search;

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
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.defaultUser.DefaultUserActivity;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.utility.User;
import com.hfad.organizationofthefestival.worker.ActiveJobsActivity;
import com.hfad.organizationofthefestival.worker.JobOffersActivity;
import com.hfad.organizationofthefestival.worker.MyApplicationsActivity;
import com.hfad.organizationofthefestival.worker.SpecializationsActivity;
import com.hfad.organizationofthefestival.worker.WorkerActivity;
import com.hfad.organizationofthefestival.worker.WorkerPrintPassActivity;

import java.util.List;
import java.util.stream.Collectors;

public class WorkerSearchActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String searcherUsername;
    private int permission;

    private WorkerSearchController searchController;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView lvSearchResults;

    private List<User> userList;

    private int searcherPermission;

    private ProgressDialog dialog;

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
        searcherUsername = intent.getStringExtra("username");
        searcherPermission = intent.getIntExtra("searcherPermission", 0);
        permission = getIntent().getIntExtra("permission", 1);
        searchController = new WorkerSearchController(this, accessToken, refreshToken);

        btnSearch.setOnClickListener(v -> {
            search();
        });

        lvSearchResults.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(WorkerSearchActivity.this, DefaultUserActivity.class);

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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        searchController.search(searched);
    }

    public void fillResults(List<User> users) {
        userList = users;
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users.stream().map(User::getUsername).collect(Collectors.toList()));
        lvSearchResults.setAdapter(specializationArrayAdapter);

        dialog.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addSpecialization) {
            Intent intent = new Intent(this, SpecializationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();

        } else if (id == R.id.applyForJob) {
            Intent intent = new Intent(this, JobOffersActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.activeJobs) {
            Intent intent = new Intent(this, ActiveJobsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.myApplications) {
            Intent intent = new Intent(this, MyApplicationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.printPass) {
            Intent intent = new Intent(this, WorkerPrintPassActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(this, WorkerSearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.worker_profile) {
            Intent intent = new Intent(this, WorkerActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", searcherUsername);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
