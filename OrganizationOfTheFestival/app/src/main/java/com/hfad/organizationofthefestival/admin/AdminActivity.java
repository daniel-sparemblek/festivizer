package com.hfad.organizationofthefestival.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.AdminAdapter;
import com.hfad.organizationofthefestival.leader.Leader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdminActivity extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<>();     //admin username
    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;

    private AdminController adminController;

    private List<Leader> leaders;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        approvalList = findViewById(R.id.approvalList);

        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");

        adminController = new AdminController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        adminController.getData();

    }

    public void adminOnClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String username = leaders.get(position).getUsername();

        System.out.println(username);

        adminController.putDecision(username, 0);
    }

    public void adminOnClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String username = leaders.get(position).getUsername();
        adminController.putDecision(username, 2);
    }

    public void fillInActivity(Admin admin) {
        leaders = admin.getLeaders();

        List<String> stringList = leadersToStrings(leaders);

        AdminAdapter adminAdapter = new AdminAdapter(this, R.layout.admin_row_layout, stringList);
        approvalList.setAdapter(adminAdapter);
        dialog.dismiss();
    }

    public List<String> leadersToStrings(List<Leader> leaders) {
        return leaders.stream()
                .map(t -> t.getUsername())
                .collect(Collectors.toList());
    }
}
