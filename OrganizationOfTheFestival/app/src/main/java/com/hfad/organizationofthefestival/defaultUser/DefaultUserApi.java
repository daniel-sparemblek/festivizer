package com.hfad.organizationofthefestival.defaultUser;

import com.hfad.organizationofthefestival.leader.Leader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface DefaultUserApi {

    @GET("leaders")
    Call<Leader> getLeader(@Query("username") String username,
                           @Header("Authorization") String authorization);
}
