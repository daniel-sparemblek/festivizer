package com.hfad.organizationofthefestival;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.ArrayAdapter;
import android.widget.ListView;
=======
import android.view.View;
import java.util.ArrayList;
>>>>>>> 345c67953d2e70bf2d63fb86d3f58c4023727bb0

public class AdminActivity extends AppCompatActivity implements AdminConnector.AdminListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        ListView approvalList = findViewById(R.id.approvalList);

        ArrayAdapter<String> approvalListAdapter = new ArrayAdapter<>(this, R.layout.admin_row_layout, );

        approvalList.setAdapter();
    }

    public void onClickGetPendingLeaders(View view) {

    }

    @Override
    public void onAdminResponse(ArrayList<String> pendingLeaders) {

    }
}
