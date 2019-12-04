package com.hfad.organizationofthefestival.login;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                if (login.isValid()) {
                    loginController.login(login);
                } else {
                    Toast.makeText(getApplicationContext(), "No fields should be empty!", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    return;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        twRegisterLink.setClickable(true);
    }


}
