package com.hfad.organizationofthefestival.defaultUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.DefaultFestivalActivity;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.worker.JobProfileActivity;
import com.hfad.organizationofthefestival.worker.Worker;
import com.hfad.organizationofthefestival.worker.jobSearch.WorkerJobSearchActivity;
import com.hfad.organizationofthefestival.worker.jobSearch.WorkerJobSearchController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultUserActivity extends AppCompatActivity {
    private DefaultUserController controller;

    private TextView tvUsername;
    private ImageView ivProfilePicture;
    private TextView tvRole;
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvEmail;
    private TextView tvPhone;
    private ListView lv;

    private String accessToken;
    private String refreshToken;
    private int searcherPermission;
    private int permission;
    private String searcherUsername;

    private ProgressDialog dialog;
    private List<Long> eventIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user_show);

        Intent intent = getIntent();
        searcherPermission = intent.getIntExtra("searcherPermission", 0);
        String username = intent.getStringExtra("username");
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        permission = intent.getIntExtra("permission", 0);
        searcherUsername = intent.getStringExtra("searcherUsername");
        getViewIds();
        controller = new DefaultUserController(DefaultUserActivity.this, accessToken, refreshToken, username);

        if (permission == 2) {
            controller.getOrganizerEvents(searcherUsername);
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.showUserProfile(permission);
    }

    public void fillInActivityLeader(Leader leader) {
        tvUsername.setText(leader.getUsername());
        tvRole.setText("LEADER");
        tvName.setText(leader.getFirstName());
        tvSurname.setText(leader.getLastName());
        tvEmail.setText(leader.getEmail());
        tvPhone.setText(leader.getPhone());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, leader.getFestivalNames());
        lv.setAdapter(arrayAdapter);
        setProfilePicture(leader.getPicture());

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(DefaultUserActivity.this, DefaultFestivalActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            Festival festival = leader.getFestival(lv.getItemAtPosition(position).toString());
            intent.putExtra("id", festival.getFestivalId());
            startActivity(intent);
        });

        dialog.dismiss();
    }

    public void fillInActivityOrganizer(Organizer organizer) {
        tvUsername.setText(organizer.getUsername());
        tvRole.setText("ORGANIZER");
        tvName.setText(organizer.getFirstName());
        tvSurname.setText(organizer.getLastName());
        tvEmail.setText(organizer.getEmail());
        tvPhone.setText(organizer.getPhone());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, organizer.getFestivals());
        lv.setAdapter(arrayAdapter);
        setProfilePicture(organizer.getPicture());

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(DefaultUserActivity.this, DefaultFestivalActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            Festival festival = organizer.getFestival(lv.getItemAtPosition(position).toString());
            intent.putExtra("id", festival.getFestivalId());
            startActivity(intent);
        });

        dialog.dismiss();
    }

    public void fillInActivityWorker(Worker worker) {
        tvUsername.setText(worker.getUsername());
        tvRole.setText("WORKER");
        tvName.setText(worker.getFirstName());
        tvSurname.setText(worker.getLastName());
        tvEmail.setText(worker.getEmail());
        tvPhone.setText(worker.getPhone());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getJobNames());
        lv.setAdapter(arrayAdapter);
        setProfilePicture(worker.getPicture());

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent;
            if (searcherPermission == 2 && eventIds.contains(worker.getJobs().get(position))){
                intent = new Intent(DefaultUserActivity.this, WorkerJobSearchActivity.class);
            } else {
                intent = new Intent(DefaultUserActivity.this, JobProfileActivity.class);
            }
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            Job job = worker.getJob(lv.getItemAtPosition(position).toString());
            intent.putExtra("username", worker.getUsername());
            intent.putExtra("job_id", job.getId());
            intent.putExtra("searcherPermission", searcherPermission);
            intent.putExtra("searcherUsername", searcherUsername);
            Gson gson = new Gson();
            intent.putExtra("job", gson.toJson(job));
            startActivity(intent);
        });

        dialog.dismiss();
    }

    private void getViewIds() {
        tvUsername = findViewById(R.id.user_name);
        ivProfilePicture = findViewById(R.id.default_profile_picture);
        tvRole = findViewById(R.id.default_role);
        tvName = findViewById(R.id.default_name);
        tvSurname = findViewById(R.id.default_surname);
        tvEmail = findViewById(R.id.default_email);
        tvPhone = findViewById(R.id.default_phone);
        lv = findViewById(R.id.my_stuffs_list);

    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }

    public void fillEventIds(EventApply[] events){
        eventIds = Arrays.stream(events)
                .map(EventApply::getEventId)
                .collect(Collectors.toList());
    }
}
