package com.hfad.organizationofthefestival.leader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LeaderClient {

    @GET("user/{username}")
    Call<Leader> getLeaderData(@Header("Authorization") String authorization,
                               @Path("username") String username);
}
