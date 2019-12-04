package com.hfad.organizationofthefestival.signup;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hfad.organizationofthefestival.leader.LeaderActivity;
import com.hfad.organizationofthefestival.OrganizerActivity;
import com.hfad.organizationofthefestival.User;
import com.hfad.organizationofthefestival.WorkerActivity;

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
        Call<RegistrationResponse> call = signupClient.signup(signup);

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(signupActivity, response.body().getAccessToken(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(signupActivity, response.raw().toString(), Toast.LENGTH_SHORT).show();
                    System.err.println("ERROR " + response.raw().toString());
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
