package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.event.Event;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.organizer.Organizer;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
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

    @GET("applications")
    Call<ApplicationResponse[]> getAuctions(@Query("leader_id") String leaderID,
                                            @Header("Authorization") String authorization);

    @GET("organizers")
    Call<Organizer[]> getUnapprovedOrganizers(@Query("festival_id_all") String leaderId,
                                              @Header("Authorization") String authorization);

    @PUT("festivals/{festival_id}")
    Call<Void> putDecision(@Path("festival_id") String festivalId, @Body HashMap<String, String> body,
                           @Header("Authorization") String authorization);

    @GET("events/active")
    Call<Event[]> getActiveEvents(@Query("festival_id") String festivalId, @Header("Authorization") String authorization);

    @GET("events/complete")
    Call<Event[]> getCompletedEvents(@Query("festival_id") String festivalId, @Header("Authorization") String authorization);

    @GET("events/pending")
    Call<Event[]> getPendingEvents(@Query("festival_id") String festivalId, @Header("Authorization") String authorization);

    @GET("event/{event_id}")
    Call<WorkingEvent> getEvent(@Path("event_id") String eventId, @Header("Authorization") String authorization);
}
