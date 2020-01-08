package com.hfad.organizationofthefestival.defaultUser;

import com.hfad.organizationofthefestival.leader.Leader;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.worker.Worker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DefaultUserApi {

    @GET("leaders")
    Call<Leader> getLeader(@Query("username") String username,
                           @Header("Authorization") String authorization);

    @GET("organizers")
    Call<Organizer> getOrganizer(@Query("username") String username,
                                 @Header("Authorization") String authorization);

    @GET("workers")
    Call<Worker> getWorker(@Query("username") String username,
                           @Header("Authorization") String authorization);

    @GET("{user}")
    Call<Object> getProfile(@Path("user") String user,
                            @Query("username") String username,
                            @Header("Authorization") String authorization);

}
