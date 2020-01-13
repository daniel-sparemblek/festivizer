package com.hfad.organizationofthefestival.festival;

import com.hfad.organizationofthefestival.organizer.Organizer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FestivalClient {

    @GET("/festival/{festivalId}")
    Call<Festival> getFestivalInfo(@Path("festivalId") String festivalId,
                                   @Header("Authorization") String authorization);

    @GET("organizers")
    Call<Organizer[]> getOrganizers(@Query("festival_id") String festivalId,
                                    @Header("Authorization") String authorization);
}
