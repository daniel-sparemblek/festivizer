package com.hfad.organizationofthefestival.login;

import android.content.Intent;
import android.widget.Toast;

import com.hfad.organizationofthefestival.organizer.OrganizerActivity;
import com.hfad.organizationofthefestival.worker.WorkerActivity;
import com.hfad.organizationofthefestival.utility.User;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController {

    private LoginActivity loginActivity;
    private LoginClient loginAPI;

    public LoginController(LoginActivity loginActivity) {
        loginAPI = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginClient.class);

        this.loginActivity = loginActivity;
    }

    public void login(final Login login) {
        Call<LoginResponse> call = loginAPI.login(login);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    enterAccount(response.body().getAccess_token(), response.body().getRefresh_token(), login.getUsername());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(loginActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(loginActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(loginActivity, "Server-side or internet error on login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enterAccount(final String accessToken, final String refreshToken, String username) {
        // This class will handle possible access token expiration using the refresh token

        Call<User> call = loginAPI.getUser(username, "Bearer " + accessToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    switchActivity(response, accessToken, refreshToken);
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(loginActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(loginActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(loginActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchActivity(Response<User> response, String accessToken, String refreshToken) {
        Intent intent;

        switch (response.body().getRole()) {
            case 1:
                intent = new Intent(loginActivity, LeaderActivity.class);
                intent.putExtra("accessToken", accessToken);
                intent.putExtra("refreshToken", refreshToken);
                intent.putExtra("username", response.body().getUsername());
                loginActivity.startActivity(intent);
                break;

            case 2:
                intent = new Intent(loginActivity, WorkerActivity.class);
                intent.putExtra("accessToken", accessToken);
                intent.putExtra("refreshToken", refreshToken);
                intent.putExtra("username", response.body().getUsername());
                loginActivity.startActivity(intent);
                break;

            case 3:
                intent = new Intent(loginActivity, OrganizerActivity.class);
                intent.putExtra("accessToken", accessToken);
                intent.putExtra("refreshToken", refreshToken);
                intent.putExtra("username", response.body().getUsername());
                loginActivity.startActivity(intent);
                break;
        }
    }
}
