package com.hfad.organizationofthefestival.festival;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface FestivalClient {

    @GET("/festival/{festivalId}")
    Call<Festival> getFestivalInfo(@Path("festivalId") String festivalId,
                                   @Header("Authorization") String authorization);
}
