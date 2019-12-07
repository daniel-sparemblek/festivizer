package com.hfad.organizationofthefestival.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.Role;
import com.hfad.organizationofthefestival.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private ImageView profile_picture;

    private EditText etUsername;
    private EditText etEmail;
    private EditText etInputPassword;
    private EditText etLastName;
    private EditText etFirstName;
    private EditText etPhone;
    private EditText etVerifyPassword;
    private AppCompatButton apbRegister;
    private TextView tvLogin;
    private Spinner sRoleChooserDropDown;

    private byte[] profilePictureInBytes;
    private Role role;
    private SignupController signupController;
    private Signup signup;

    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupController = new SignupController(this);

        findIds();
        setupRoleChooser();

        apbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePictureInBytes = pictureToByteArray(((BitmapDrawable) profile_picture.getDrawable()).getBitmap());
                role = getRoleEnum(sRoleChooserDropDown.getSelectedItem().toString());

                signup = new Signup(etUsername.getText().toString(),
                        etInputPassword.getText().toString(),
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        profilePictureInBytes.toString(),
                        etPhone.getText().toString(),
                        etEmail.getText().toString(),
                        role.getValue());
                String textFieldsStatus = signup.checkInput(etVerifyPassword.getText().toString());
                if (textFieldsStatus == null) {
                    signupController.signUp(signup);
                }
                Toast.makeText(SignupActivity.this, textFieldsStatus, Toast.LENGTH_SHORT).show();

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLogin.setEnabled(false);
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.profile_picture);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        apbRegister.setEnabled(true);
        tvLogin.setEnabled(true);
    }

    private static byte[] pictureToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] profilePictureInByte;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        profilePictureInByte = baos.toByteArray();
        return profilePictureInByte;
    }

    private static Role getRoleEnum(String role) {
        switch (role) {
            case "LEADER":
                return Role.LEADER;
            case "ORGANIZER":
                return Role.ORGANIZER;
            default:
                return Role.WORKER;
        }
    }

    private void findIds() {
        profile_picture = findViewById(R.id.profile_picture);
        etUsername = findViewById(R.id.input_username);
        etEmail = findViewById(R.id.input_email);
        etInputPassword = findViewById(R.id.input_password);
        etLastName = findViewById(R.id.input_surname);
        etFirstName = findViewById(R.id.input_name);
        etPhone = findViewById(R.id.input_phone);
        apbRegister = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.link_login);
        etVerifyPassword = findViewById(R.id.verify_password);
        sRoleChooserDropDown = findViewById(R.id.roleChooser);
    }

    private void setupRoleChooser() {
        String[] items = new String[]{Role.LEADER.toString(), Role.ORGANIZER.toString(), Role.WORKER.toString()}; //Leader, Organizer, Worker
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sRoleChooserDropDown.setAdapter(adapter);
    }
}
