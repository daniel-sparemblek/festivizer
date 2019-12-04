package com.hfad.organizationofthefestival.signup;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hfad.organizationofthefestival.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupController {

    private SignupActivity signupActivity;

    public SignupController(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://kaogrupa.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    SignupClient signupClient = retrofit.create(SignupClient.class);

    public void signUp(Signup signup) {
        Call<RegistrationResponse> callSignup = signupClient.signup(signup);

        callSignup.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getMessage().equals("Username already exists!")) {
                        Toast.makeText(signupActivity, "User already exists!", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage().equals("Phone is already in use")) {
                        Toast.makeText(signupActivity, "Phone already exists!", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage().equals("Email already exists")) {
                        Toast.makeText(signupActivity, "Email already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(signupActivity, "Great! You successfully signed up! Please login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signupActivity, LoginActivity.class);
                        intent.putExtra("accessToken", response.body().getAccess_token());
                        intent.putExtra("refreshToken", response.body().getRefresh_token());
                        signupActivity.startActivity(intent);
                    }
                } else {
                    Toast.makeText(signupActivity, response.raw().toString(), Toast.LENGTH_SHORT).show();
                    System.err.println("ERROR " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(signupActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
                Log.d("error", t.toString());
            }
        });
    }
}
