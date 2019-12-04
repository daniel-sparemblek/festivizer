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
import com.hfad.organizationofthefestival.leader.LeaderActivity;
import com.hfad.organizationofthefestival.OrganizerActivity;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.ServerStatus;
import com.hfad.organizationofthefestival.signup.SignupActivity;
import com.hfad.organizationofthefestival.UnconfirmedActivity;
import com.hfad.organizationofthefestival.WorkerActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity{

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

                Login login = new Login(etEmail.getText().toString(), etPassword.getText().toString());
                loginController.login(login);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        twRegisterLink.setClickable(true);
    }


}
