package com.hfad.organizationofthefestival.festival.creation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

public class CreateFastivalActivity extends AppCompatActivity {

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

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
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

    private String getStringPicture(){
        return null;
    }
}
