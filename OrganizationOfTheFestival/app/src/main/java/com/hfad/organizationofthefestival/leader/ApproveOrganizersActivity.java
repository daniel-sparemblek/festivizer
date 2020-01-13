package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.AdminAdapter;
import com.hfad.organizationofthefestival.organizer.Organizer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApproveOrganizersActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String festivalId;

    private ListView lvOrganizers;
    private List<Organizer> organizerList;

    private ApproveOrganizersController approveOrganizersController;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_organizers);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getStringExtra("festival_id");


        lvOrganizers = findViewById(R.id.org_approval_list);

        approveOrganizersController = new ApproveOrganizersController(this, accessToken, festivalId, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        approveOrganizersController.getUnapprovedOrganizers();
    }

    public void fillInActivity(Organizer[] body) {
        List<String> organizers = organizersToStrings(body);
        AdminAdapter adapter = new AdminAdapter(this, R.layout.admin_row_layout, organizers);
        lvOrganizers.setAdapter(adapter);

        dialog.dismiss();
    }

    public void adminOnClickAccept(View view) {
        final int position = lvOrganizers.getPositionForView((LinearLayout) view.getParent());

        Organizer organizer = organizerList.get(position);
        approveOrganizersController.putDecision(organizer.getId(), 2);
    }

    public void adminOnClickDecline(View view) {
        final int position = lvOrganizers.getPositionForView((LinearLayout) view.getParent());

        Organizer organizer = organizerList.get(position);
        approveOrganizersController.putDecision(organizer.getId(), 3);
    }

    public List<String> organizersToStrings(Organizer[] organizers) {
        organizerList = Arrays.asList(organizers);

        List<String> stringList = organizerList.stream()
                .map(t -> t.getUsername())
                .collect(Collectors.toList());
        return stringList;
    }
}
