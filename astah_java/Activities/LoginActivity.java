package com.hfad.organizationofthefestival.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.signup.SignupActivity;

public class LoginActivity extends AppCompatActivity {

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

        twRegisterLink.setOnClickListener(v -> {
            twRegisterLink.setClickable(false);
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            btnLogin.setEnabled(false);

            Login login = new Login(etEmail.getText().toString(), etPassword.getText().toString());
            if (login.isValid()) {
                loginController.login(login);
            } else {
                Toast.makeText(getApplicationContext(), "No fields should be empty!", Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true    );
                return;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        twRegisterLink.setClickable(true);
    }

    public Button getBtnLogin() {
        return btnLogin;
    }
}
