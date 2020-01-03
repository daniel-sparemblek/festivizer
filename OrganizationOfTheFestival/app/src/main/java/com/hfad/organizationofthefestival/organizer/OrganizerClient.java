package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.festival.Festival;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrganizerClient {
    @GET("organizers")
    Call<Organizer> getData(@Query("username") String username,
                            @Header("Authorization") String authorization);

    @GET("festivals")
    Call<Festival[]> getFestivals(@Header("Authorization") String authorization);
}
