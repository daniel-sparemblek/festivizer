package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements AdminConnector.AdminListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void onClickGetPendingLeaders(View view) {

    }

    @Override
    public void onAdminResponse(ArrayList<String> pendingLeaders) {

    }
}
