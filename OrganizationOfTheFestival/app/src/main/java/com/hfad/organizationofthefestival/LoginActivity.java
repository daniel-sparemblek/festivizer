package com.hfad.organizationofthefestival;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements Connector.ServerListener {

    private EditText email;
    private EditText password;
    private TextView register;
    private Button login;
    private String user_email;
    private String user_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        register = findViewById(R.id.link_signup);
        login = findViewById(R.id.btn_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = email.getText().toString();
                user_pwd = password.getText().toString();
                Connector.logIn(user_email, securePassword(user_pwd), LoginActivity.this);
            }
        });
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
        if(status == ServerStatus.SUCCESS)
            startActivity(new Intent(this, WorkerActivity.class));

        if(status == ServerStatus.ADMIN) {
            startActivity(new Intent(this, AdminActivity.class));
        }

        if(status == ServerStatus.ORGANIZER) {
            startActivity(new Intent(this, OrganizerActivity.class));
        }

        if(status == ServerStatus.SERVER_DOWN) {
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Server is currently down. Please try again later";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.NO_USERNAME) {
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Username doesn't exist. Please register";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.WRONG_PASSWORD){
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Wrong password";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }

    @Override
    public void onRegisterResponse(ServerStatus status) {

    }
}
