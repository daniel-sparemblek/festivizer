package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;

import java.util.ArrayList;
import java.util.List;


public class SpecializationsActivity extends AppCompatActivity {

    private SpecializationsController specializationsController;
    private String accessToken;
    private String refreshToken;
    private String username;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView lvSpecializations;

    private TextView tvCreateSpec;
    private Button btnCreateSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_add_specialization);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        this.specializationsController = new SpecializationsController(this, accessToken, username, refreshToken);
        specializationsController.getSpecializations();
    }

    public void fillInActivity(Specialization[] specializations) {
        lvSpecializations = findViewById(R.id.worker_search_specialization);
        tvSearch = findViewById(R.id.worker_searchTxt);
        btnSearch = findViewById(R.id.worker_searchBtn);
        btnCreateSpec = findViewById(R.id.btn_create_spec);
        tvCreateSpec = findViewById(R.id.tv_create_spec);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, specializationsToString(specializations));
        lvSpecializations.setAdapter(specializationArrayAdapter);

    }

    public List<String> specializationsToString(Specialization[] specializations) {
        List<String> specStrings = new ArrayList<>();

        for(Specialization specialization : specializations) {
            specStrings.add(specialization.toString());
        }
        return specStrings;
    }

    public void createSpecialization(View view) {
        String specName = tvCreateSpec.getText().toString();

        if(specName.isEmpty()) {
            Toast.makeText(this, "Name of specialization can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        specializationsController.addSpecialization(specName);
    }

}
