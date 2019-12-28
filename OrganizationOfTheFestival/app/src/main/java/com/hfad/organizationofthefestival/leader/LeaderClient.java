package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festivals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LeaderClient {

    @GET("festivals")
    Call<Festivals> getFestivals(@Header("Authorization") String authorization);
}
