package com.hfad.organizationofthefestival.festival.creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.io.ByteArrayOutputStream;

public class CreateFastivalActivity extends AppCompatActivity {

    private FestivalCreationController controller;
    private String accessToken;
    private String refreshToken;

    private ImageView ivLogo;
    private EditText etName;
    private EditText etDescription;
    private EditText etDuration;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_festival_creation);

        findViews();
        controller = new FestivalCreationController(this);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");

        btCreate.setOnClickListener(v -> {
            Festival festival = new Festival(etName.getText().toString(),
                    etDescription.getText().toString(),
                    getPictureString(),
                    Integer.parseInt(etDuration.getText().toString()));
            controller.createFestival(festival, accessToken);
        });
    }

    private void findViews() {
        ivLogo = findViewById(R.id.festivalLogo);
        etName = findViewById(R.id.name);
        etDescription = findViewById(R.id.desc);
        etDuration = findViewById(R.id.duration);
        etStartTime = findViewById(R.id.startTime);
        etEndTime = findViewById(R.id.endTime);
        btCreate = findViewById(R.id.createBtn);
    }

    private String getPictureString() {
        Bitmap bitmap = ((BitmapDrawable) ivLogo.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] pictureByte = bos.toByteArray();
        return Base64.encodeToString(pictureByte, Base64.DEFAULT);
    }
}
