package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.festival.FestivalsResponse;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.JobApply;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrganizerClient {
    @GET("organizers")
    Call<Organizer> getData(@Query("username") String username,
                            @Header("Authorization") String authorization);

    @GET("organizers/festivals")
    Call<FestivalsResponse[]> getFestivals(@Header("Authorization") String authorization);

    @POST("festival/{festivalId}/apply")
    Call<Void> apply(@Header("Authorization") String authorization,
                     @Path("festivalId") String festivalId);

    @GET("events")
    Call<EventApply[]> getAllEvents(@Query("username") String username,
                                    @Header("Authorization") String authorization);

    @GET("jobs")
    Call<JobApply[]> getAllJobs(@Query("username") String username,
                                @Header("Authorization") String authorization);
}
