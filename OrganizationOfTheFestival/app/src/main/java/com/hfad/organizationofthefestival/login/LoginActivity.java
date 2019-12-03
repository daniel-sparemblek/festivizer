package com.hfad.organizationofthefestival.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hfad.organizationofthefestival.admin.AdminActivity;
import com.hfad.organizationofthefestival.Connector;
import com.hfad.organizationofthefestival.LeaderActivity;
import com.hfad.organizationofthefestival.OrganizerActivity;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.ServerStatus;
import com.hfad.organizationofthefestival.signup.SignupActivity;
import com.hfad.organizationofthefestival.UnconfirmedActivity;
import com.hfad.organizationofthefestival.WorkerActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements Connector.ServerListener {

    /**
     * User's email EditText field.
     */
    private EditText etEmail;
    /**
     * User's password EditText field.
     */
    private EditText etPassword;
    /**
     * TextView that contains link to the RegisterActivity if user wants to register instead of login.
     */
    private TextView twRegisterLink;
    private Button btnLogin;

    private String user_email;
    private String user_pwd;

    private LoginController loginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = new LoginController(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        twRegisterLink = findViewById(R.id.tw_register_link);
        btnLogin = findViewById(R.id.btn_login);

        etEmail.requestFocus();

        twRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twRegisterLink.setClickable(false);
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setEnabled(false);
                Connector.logIn(etEmail.getText().toString().trim(), securePassword(etPassword.getText().toString()), LoginActivity.this);
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

        if(status == ServerStatus.SUCCESS) {
            Intent intent = new Intent(this, WorkerActivity.class);
            intent.putExtra("USERNAME", user_email);
            intent.putExtra("PASSWORD", securePassword(user_pwd));
            startActivity(intent);
        }

        if(status == ServerStatus.ADMIN) {
            Intent intent = new Intent(this, AdminActivity.class);
            intent.putExtra("USERNAME", user_email);
            intent.putExtra("PASSWORD", securePassword(user_pwd));
            startActivity(intent);
        }

        if(status == ServerStatus.ORGANIZER) {
            Intent intent = new Intent(this, OrganizerActivity.class);
            intent.putExtra("USERNAME", user_email);
            intent.putExtra("PASSWORD", securePassword(user_pwd));
            startActivity(intent);
        }

        if(status == ServerStatus.LEADER_CONFIRMED) {
            Intent intent = new Intent(this, LeaderActivity.class);
            intent.putExtra("USERNAME", user_email);
            intent.putExtra("PASSWORD", securePassword(user_pwd));
            startActivity(intent);
        }

        if(status == ServerStatus.LEADER_PENDING) {
            Intent intent = new Intent(this, UnconfirmedActivity.class);
            startActivity(intent);
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
            etEmail.requestFocus();
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Username doesn't exist. Please register";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        if(status == ServerStatus.WRONG_PASSWORD) {
            etPassword.requestFocus();
            Context context;
            Toast toast;
            context = getApplicationContext();
            CharSequence message = "Wrong password";
            int duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        btnLogin.setEnabled(true);
    }

    @Override
    public void onRegisterResponse(ServerStatus status) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        twRegisterLink.setClickable(true);
    }


}
