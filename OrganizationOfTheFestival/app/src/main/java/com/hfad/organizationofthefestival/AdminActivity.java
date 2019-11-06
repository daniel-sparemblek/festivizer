package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        ListView approvalList = findViewById(R.id.approvalList);

        ArrayAdapter<String> approvalListAdapter = new ArrayAdapter<>(this, R.layout.admin_row_layout, );

        approvalList.setAdapter();
    }
}
