package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.Festivals;
import com.hfad.organizationofthefestival.utility.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LeaderClient {

    @GET("user/{username}")
    Call<User> getLeaderData(@Path("username") String username,
                             @Header("Authorization") String authorization);

    @GET("festivals")
    Call<List<Festival>> getFestivals(@Query("leader_id") int id,
                                       @Header("Authorization") String authorization);
}
