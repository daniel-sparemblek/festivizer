package com.hfad.organizationofthefestival.defaultUser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.worker.Worker;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user_show);

        Intent intent = getIntent();
        int permission = intent.getIntExtra("permission", 0);
        String username = intent.getStringExtra("username");
        String accessToken = intent.getStringExtra("accessToken");
        String refreshToken = intent.getStringExtra("refreshToken");
        getViewIds();
        controller = new DefaultUserController(DefaultUserActivity.this, accessToken, refreshToken, username);
        controller.showUserProfile(permission);
    }

    public void fillInActivityLeader(Leader leader){
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
    }

    public void fillInActivityOrganizer(Organizer organizer){
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
    }

    public void fillInActivityWorker(Worker worker){
        tvUsername.setText(worker.getUsername());
        tvRole.setText("WORKER");
        tvName.setText(worker.getFirstName());
        tvSurname.setText(worker.getLastName());
        tvEmail.setText(worker.getEmail());
        tvPhone.setText(worker.getPhone());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getJobs());
        lv.setAdapter(arrayAdapter);
        setProfilePicture(worker.getPicture());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getViewIds(){
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
}
