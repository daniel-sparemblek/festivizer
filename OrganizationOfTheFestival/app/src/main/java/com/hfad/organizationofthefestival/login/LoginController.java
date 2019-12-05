package com.hfad.organizationofthefestival.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.hfad.organizationofthefestival.OrganizerActivity;
import com.hfad.organizationofthefestival.User;
import com.hfad.organizationofthefestival.WorkerActivity;
import com.hfad.organizationofthefestival.leader.LeaderActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController {

    private LoginActivity loginActivity;

    public LoginController(LoginActivity loginActivity) {

        this.loginActivity = loginActivity;
    }

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://kaogrupa.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    private LoginClient loginClient = retrofit.create(LoginClient.class);

    public void login(final Login login) {
        Call<LoginResponse> call = loginClient.login(login);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    enterAccount(response.body().getAccess_token(), response.body().getRefresh_token(), login.getUsername());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(loginActivity, errorObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(loginActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
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

    public void enterAccount(final String accessToken, String refreshToken, String username) {
        Call<User> call = loginClient.getUser(username, accessToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Sending the username in the intent may be needed
                    switch (response.body().getRole()) {
                        case "LEADER":
                            Intent intent = new Intent(loginActivity, LeaderActivity.class);
                            intent.putExtra("accessToken", accessToken);
                            loginActivity.startActivity(intent);
                            break;

                        case "WORKER":
                            Intent intent2 = new Intent(loginActivity, WorkerActivity.class);
                            intent2.putExtra("accessToken", accessToken);
                            loginActivity.startActivity(intent2);
                            break;

                        case "ORGANIZER":
                            Intent intent3 = new Intent(loginActivity, OrganizerActivity.class);
                            intent3.putExtra("accessToken", accessToken);
                            loginActivity.startActivity(intent3);
                            break;
                    }
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(loginActivity, errorObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(loginActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
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
}
