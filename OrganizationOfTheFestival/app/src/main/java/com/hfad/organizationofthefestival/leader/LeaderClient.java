package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.utility.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LeaderClient {
    @GET("leaders")
    Call<Leader> getAll(@Query("username") String username,
                        @Header("Authorization") String authorization);
}
