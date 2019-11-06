package com.hfad.organizationofthefestival;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements Connector.ServerListener {

    EditText email;
    EditText password;
    TextView register;
    Button login;
    String user_email;
    String user_pwd;

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
                Connector.logIn(user_email, user_pwd, LoginActivity.this);
            }
        });
    }

    @Override
    public void onLogInResponse(ServerStatus status) {
        if(status == ServerStatus.SUCCESS)
            //startActivity(new Intent(this, MainActivity.class));

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
