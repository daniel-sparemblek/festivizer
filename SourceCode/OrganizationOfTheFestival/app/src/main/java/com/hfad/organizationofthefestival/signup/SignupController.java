package com.hfad.organizationofthefestival.signup;

import android.content.Intent;
import android.widget.Toast;

import com.hfad.organizationofthefestival.login.LoginActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupController {

    private SignupActivity signupActivity;
    private SignupClient api;

    public SignupController(SignupActivity signupActivity) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SignupClient.class);

        this.signupActivity = signupActivity;
    }

    public void signUp(final Signup signup) {
        Call<RegistrationResponse> callSignup = api.signup(signup);

        callSignup.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(signupActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signupActivity, LoginActivity.class);
                    signupActivity.startActivity(intent);
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(signupActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(signupActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(signupActivity, "unable to connect :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
