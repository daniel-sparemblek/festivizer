package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.utility.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LeaderClient {

    @GET("user/{username}")
    Call<User> getLeaderData(@Path("username") String username,
                             @Header("Authorization") String authorization);
}
