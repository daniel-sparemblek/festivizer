package com.hfad.organizationofthefestival;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity implements Connector.ServerListener {

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

    private String usernameString;
    private String nameString;
    private String lastNameString;
    private String phoneString;
    private String emailString;
    private String pwd1String;
    private String pwd2String;
    private Role userRole;

    private Bitmap bitmap;
    private ByteArrayOutputStream baos;
    private byte[] profilePictureInByte;

    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        username.requestFocus();

        //SetUp roleChoser dropdown list
        String[] items = new String[]{Role.LEADER.toString(), Role.ORGANIZER.toString(), Role.WORKER.toString()}; //Leader, Organizer, Worker
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        roleChooserDropDown.setAdapter(adapter);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Converting profile_picture to byte array
                bitmap = ((BitmapDrawable) profile_picture.getDrawable()).getBitmap();
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                profilePictureInByte = baos.toByteArray();

                //Taking text from all the EditTexts and converting it to string
                usernameString = username.getText().toString();
                nameString = name.getText().toString();
                lastNameString = lastName.getText().toString();
                phoneString = phone.getText().toString();
                emailString = email.getText().toString();
                pwd1String = password1.getText().toString();
                pwd2String = password2.getText().toString();

                if(roleChooserDropDown.getSelectedItem().toString().equals("LEADER")) {
                    userRole = Role.LEADER;
                } else if(roleChooserDropDown.getSelectedItem().toString().equals("ORGANIZER")) {
                    userRole = Role.ORGANIZER;
                } else if(roleChooserDropDown.getSelectedItem().toString().equals("WORKER")) {
                    userRole = Role.WORKER;
                }


                //Calling the method register from class Connector if email is valid
                if (emailIsInvalid(emailString)){
                    email.setText("");
                    email.requestFocus();

                    Context context;
                    Toast toast;
                    context = getApplicationContext();
                    CharSequence message = "Email format is invalid.";
                    int duration = Toast.LENGTH_SHORT;
                    toast = Toast.makeText(context, message, duration);
                    toast.show();
                } else if (!verifyPassword(pwd1String, pwd2String)) {
                    password1.requestFocus();

                    Context context;
                    Toast toast;
                    context = getApplicationContext();
                    CharSequence message = "Passwords do not match!";
                    int duration = Toast.LENGTH_SHORT;
                    toast = Toast.makeText(context, message, duration);
                    toast.show();
                } else {
                    register.setClickable(false);
                    Connector.register(usernameString, securePassword(pwd1String), nameString, lastNameString, profilePictureInByte, phoneString, emailString, userRole, SignupActivity.this);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setClickable(false);
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK,
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

    private static boolean verifyPassword(String password1, String password2){
        return password1.equals(password2);
    }

    private String securePassword(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public void onLogInResponse(ServerStatus status) {

    }

    @Override
    public void onRegisterResponse(ServerStatus status) {
        if(status == ServerStatus.SUCCESS){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Well done! Please login";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        }
        if(status == ServerStatus.SERVER_DOWN){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Server is currently down. Please try again later";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.EMAIL_EXISTS){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "This email is already in use";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.USERNAME_EXISTS){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Username is already taken";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.PHONE_EXISTS){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "This phone is already linked to another account";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.UNKNOWN){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Something terrible happened. Please try again later";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        register.setClickable(false);
    }

    private static boolean emailIsInvalid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return !pat.matcher(email).matches();
    }
}
