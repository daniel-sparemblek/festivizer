package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.WorkerSearchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SpecializationsActivity extends AppCompatActivity {

    private SpecializationsController specializationsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private int permission;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView lvSpecializations;

    private TextView tvCreateSpec;
    private Button btnCreateSpec;

    private List<Specialization> specializations;
    private List<Specialization> mySpecializations;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_add_specialization);

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        toolbar.setTitle("Add Specializations");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", -1);
        getViewReferences();

        this.specializationsController = new SpecializationsController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        specializationsController.getSpecializationsByUsername();
    }

    public void getViewReferences() {
        lvSpecializations = findViewById(R.id.worker_search_specialization);
        tvSearch = findViewById(R.id.worker_searchTxt);
        btnSearch = findViewById(R.id.worker_searchBtn);
        btnCreateSpec = findViewById(R.id.btn_create_spec);
        tvCreateSpec = findViewById(R.id.tv_create_spec);


        lvSpecializations.setOnItemClickListener((parent, view, position, id) -> {
            if (mySpecializations.contains(specializations.get(position))) {
                Toast.makeText(SpecializationsActivity.this, "You already own this specialization!", Toast.LENGTH_SHORT).show();
                return;
            }
            specializationsController
                    .addSpecializationToWorker(SpecializationsActivity.this.specializations.get(position));
        });
    }

    public void fillInActivity(Specialization[] specializations) {
        this.specializations = Arrays.asList(specializations);
        List<String> specStrings = specializationsToString(specializations);

        char c = 0x2714;

        for (int i = 0; i < this.specializations.size(); i++) {
            if (mySpecializations.contains(this.specializations.get(i))) {
                specStrings.set(i, specStrings.get(i) + c);
            }
        }
        this.specializations.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        specStrings.sort(String::compareTo);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specStrings);
        lvSpecializations.setAdapter(specializationArrayAdapter);
        dialog.dismiss();

    }

    public List<String> specializationsToString(Specialization[] specializations) {
        List<String> specStrings = new ArrayList<>();

        for (Specialization specialization : specializations) {
            specStrings.add(specialization.toString());
        }
        return specStrings;
    }

    public void createSpecialization(View view) {
        String specName = tvCreateSpec.getText().toString();

        if (specName.isEmpty()) {
            Toast.makeText(this, "Name of specialization can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        specializationsController.addSpecialization(specName);
    }

    public void searchSpecialization(View view) {
        String searched = tvSearch.getText().toString();

        specializationsController.searchSpecialization(searched);
    }

    public void setMySpecializations(Specialization[] specializations) {
        this.mySpecializations = Arrays.asList(specializations);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
            dialog = new ProgressDialog(this);
            dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            specializationsController.getSpecializations();

        } else if (id == R.id.applyForJob) {
            Intent intent = new Intent(this, JobOffersActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.activeJobs) {
            Intent intent = new Intent(this, ActiveJobsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.myApplications) {
            Intent intent = new Intent(this, MyApplicationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.printPass) {
            Intent intent = new Intent(this, WorkerPrintPassActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(this, WorkerSearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        } else if (id == R.id.worker_profile) {
            Intent intent = new Intent(this, WorkerActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", permission);
            this.startActivity(intent);
            finish();
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
