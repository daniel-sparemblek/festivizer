package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity implements AdminConnector.AdminListener {

    private ArrayList<String> data = new ArrayList<>();
    private ListView approvalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        approvalList = findViewById(R.id.approvalList);

        AdminConnector.getPendingLeaders("Bartz", "134", this);


    }

    public void onClickGetPendingLeaders(View view) {
        AdminConnector.getPendingLeaders("Bartz", "134", this);
    }

    @Override
    public void onAdminResponse(ArrayList<String> pendingLeaders) {
        data = pendingLeaders;
        ArrayAdapter<String> approvalListAdapter = new ArrayAdapter<>(this, R.layout.skroznovi, data);
        approvalList.setAdapter(approvalListAdapter);
    }
}
