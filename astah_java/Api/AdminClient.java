package com.hfad.organizationofthefestival.admin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface AdminClient {
    @GET("/admins")
    Call<Admin> getAdmin(@Header("Authorization") String authorization);

    @PUT("/users")
    Call<Void> putDecision(@Body HashMap<String, String> body, @Header("Authorization") String authorization);
}
