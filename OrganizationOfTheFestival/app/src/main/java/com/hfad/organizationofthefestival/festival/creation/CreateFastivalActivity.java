package com.hfad.organizationofthefestival.festival.creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;

public class CreateFastivalActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

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

    private Festival festival;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_screen_festival_creation);

        findViews();
        controller = new FestivalCreationController(this);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CreateFastivalActivity.this.checkEntry() == false) {
                    return;
                }
                festival = new Festival(etName.getText().toString(),
                        etDescription.getText().toString(),
                        CreateFastivalActivity.this.getPictureString(),
                        ZonedDateTime.parse(etStartTime.getText().toString()),
                        ZonedDateTime.parse(etEndTime.getText().toString()));
                controller.createFestival(festival, accessToken);
                CreateFastivalActivity.this.returnToLeaderActivity();
            }
        });

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                CreateFastivalActivity.this.startActivityForResult(intent1, PICK_IMAGE_REQUEST);
            }
        });
    }

    private void returnToLeaderActivity() {
        Intent intent = new Intent(CreateFastivalActivity.this, LeaderActivity.class);
        startActivity(intent);
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

    private boolean checkEntry(){
        if ("".equals(etName.getText().toString())){
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etDescription.getText().toString())){
            Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etDuration.getText().toString())){
            Toast.makeText(this, "Duration can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if ("".equals(etStartTime.getText().toString()) || "".equals(etEndTime.getText().toString())){
            Toast.makeText(this, "Start and and time must be specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
