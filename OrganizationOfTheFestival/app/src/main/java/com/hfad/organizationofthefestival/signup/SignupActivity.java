package com.hfad.organizationofthefestival.signup;

import android.content.Context;
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

import com.hfad.organizationofthefestival.Connector;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.Role;
import com.hfad.organizationofthefestival.ServerStatus;
import com.hfad.organizationofthefestival.User;
import com.hfad.organizationofthefestival.login.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private ImageView profile_picture;
    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText lastName;
    private EditText name;
    private EditText phone;
    private AppCompatButton register;
    private TextView login;
    private EditText password2;
    private Spinner roleChooserDropDown;
    private byte[] profilePictureInBytes;
    private Role role;
    private SignupController signupController;
    private Signup signup;
    private User user;

    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupController = new SignupController(this);
        profile_picture = findViewById(R.id.profile_picture);
        username = findViewById(R.id.input_username);
        email = findViewById(R.id.input_email);
        password1 = findViewById(R.id.input_password);
        lastName = findViewById(R.id.input_surname);
        name = findViewById(R.id.input_name);
        phone = findViewById(R.id.input_phone);
        register = findViewById(R.id.btn_signup);
        login = findViewById(R.id.link_login);
        password2 = findViewById(R.id.verify_password);
        roleChooserDropDown = findViewById(R.id.roleChooser);

        //SetUp roleChooser dropdown list
        String[] items = new String[]{Role.LEADER.toString(), Role.ORGANIZER.toString(), Role.WORKER.toString()}; //Leader, Organizer, Worker
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        roleChooserDropDown.setAdapter(adapter);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePictureInBytes = pictureToByteArray(((BitmapDrawable) profile_picture.getDrawable()).getBitmap());
                role = getRoleEnum(roleChooserDropDown.getSelectedItem().toString());
                signup = new Signup(username.getText().toString(),
                        password1.getText().toString(),
                        password2.getText().toString(),
                        name.getText().toString(),
                        lastName.getText().toString(),
                        profilePictureInBytes,
                        phone.getText().toString(),
                        email.getText().toString(),
                        role);
                if (signup.check() == null) {
                    user = signup.makeUser();
                    signupController.signUp(signup);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
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
        register.setEnabled(true);
        login.setEnabled(true);
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
}
