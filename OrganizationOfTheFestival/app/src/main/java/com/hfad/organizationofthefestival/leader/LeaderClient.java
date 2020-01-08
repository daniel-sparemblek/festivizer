package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festival;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LeaderClient {
    @GET("leaders")
    Call<Leader> getLeaderData(@Query("username") String username,
                               @Header("Authorization") String authorization);

    @GET("festivals/{type}")
    Call<Festival[]> getFestivals(@Path("type") String type,
                                  @Query("leader_id") String leader_id,
                                  @Header("Authorization") String authorization);

    @GET("festivals")
    Call<Festival[]> getLeaderFestivals(@Query("leader_id") String leaderId,
                                        @Header("Authorization") String authorization);

    @GET("auctions")
    Call<Application[]> getAuctions(@Query("leader_id") String leaderID,
                                    @Header("Authorization") String authorization);

}
