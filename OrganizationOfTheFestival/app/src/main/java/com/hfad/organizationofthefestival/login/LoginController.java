package com.hfad.organizationofthefestival.login;

import android.widget.Toast;

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

    public void login(Login login) {
        Call<LoginResponse> call = loginClient.login(login);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(loginActivity, "*?=CjoncN)(Z)(#j09uf0=()FZ=.s'0-e _?=fkPk þø^[þ@}¶→€", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(loginActivity, "Client error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(loginActivity, "Server-side or internet error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
