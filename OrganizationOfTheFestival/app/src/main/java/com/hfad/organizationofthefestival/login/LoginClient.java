package com.hfad.organizationofthefestival.login;

import com.hfad.organizationofthefestival.utility.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginClient {

    @POST("login")
    Call<LoginResponse> login(@Body Login login);

    @GET("user/{username}")
    Call<User> getUser(@Path("username") String username,
                       @Header("Authorization") String authorization);
}
