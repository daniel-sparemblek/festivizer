package com.hfad.organizationofthefestival;

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

import com.hfad.organizationofthefestival.login.LoginActivity;

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
                register.setEnabled(false);
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
                    toast("Email format is invalid.");
                    register.setEnabled(true);
                } else if (!verifyPassword(pwd1String, pwd2String)) {
                    password1.requestFocus();
                    toast("Passwords do not match!");
                    register.setEnabled(true);
                } else {
                    Connector.register(usernameString, securePassword(pwd1String), nameString, lastNameString, profilePictureInByte, phoneString, emailString, userRole, SignupActivity.this);
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

    private void toast(CharSequence message) {
        Context context;
        Toast toast;
        context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    public void onRegisterResponse(ServerStatus status) {
        if(status == ServerStatus.SUCCESS){
            toast("Well done! Please login");
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        }
        if(status == ServerStatus.SERVER_DOWN){
            toast("Server is currently down. Please try again later");
        }
        if(status == ServerStatus.EMAIL_EXISTS){
            toast("This email is already in use");
        }
        if(status == ServerStatus.USERNAME_EXISTS){
            toast("Username is already taken");
        }
        if(status == ServerStatus.PHONE_EXISTS){
            toast("This phone is already linked to another account");
        }
        if(status == ServerStatus.UNKNOWN){
            toast("Something terrible happened. Please try again later");
        }
        register.setEnabled(true);
    }

    private static boolean emailIsInvalid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return !pat.matcher(email).matches();
    }

    @Override
    public void onResume(){
        super.onResume();
        register.setEnabled(true);
        login.setEnabled(true);
    }
}
