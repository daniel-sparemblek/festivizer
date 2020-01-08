package com.hfad.organizationofthefestival.admin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AdminClient {
    @GET("/admins")
    Call<Admin> getAdmin(@Header("Authorization") String authorization);
}
