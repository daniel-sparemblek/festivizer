package com.hfad.organizationofthefestival.signup;

import android.content.Intent;
import android.widget.Toast;

import com.hfad.organizationofthefestival.LeaderActivity;
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
            .baseUrl("https://barbil.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    SignupClient signupClient = retrofit.create(SignupClient.class);

    private void signUp(User user){
        Call<User> call = signupClient.signup(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    switch (response.body().getRole()){
                        case LEADER:
                            signupActivity.startActivity(new Intent(signupActivity, LeaderActivity.class));
                            break;

                        case WORKER:
                            signupActivity.startActivity(new Intent(signupActivity, WorkerActivity.class));
                            break;

                        case ORGANIZER:
                            signupActivity.startActivity(new Intent(signupActivity, OrganizerActivity.class));
                            break;
                    }
                } else {
                    Toast.makeText(signupActivity, "signup error :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(signupActivity, "server error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
