package com.hfad.organizationofthefestival.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginClient {

    @POST("login")
    Call<LoginResponse> login(@Body Login login);
}
