package com.hfad.organizationofthefestival.defaultUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hfad.organizationofthefestival.R;

public class DefaultUserActivity extends AppCompatActivity {
    private DefaultUserController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user_show);

        Intent intent = getIntent();
        int permission = intent.getIntExtra("permission", 0);
        String username = intent.getStringExtra("username");
        String accessToken = intent.getStringExtra("accessToken");

        controller.showUserProfile(permission, username, accessToken);
    }
}
