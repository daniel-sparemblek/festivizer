package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.Festivals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LeaderClient {
    @GET("leaders")
    Call<Leader> getLeaderData(@Query("username") String username,
                               @Header("Authorization") String authorization);

    @GET("festivals/complete")
    Call<Festivals> getCompletedFestivals(@Query("leader_id") String leader_id,
                                          @Header("Authorization") String authorization);

    @GET("festivals")
    Call<Festival[]> getLeaderFestivals(@Query("leader_id") String leaderId, @Header("Authorization") String authorization);

}
