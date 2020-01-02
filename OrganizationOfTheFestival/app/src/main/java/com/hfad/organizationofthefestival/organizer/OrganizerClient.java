package com.hfad.organizationofthefestival.organizer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrganizerClient {
    @GET("organizers")
    Call<Organizer> getData(@Query("username") String username,
                            @Header("Authorization") String authorization);
}
