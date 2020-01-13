package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.festival.FestivalsResponse;
import com.hfad.organizationofthefestival.utility.ApplicationAuction;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.NewJob;
import com.hfad.organizationofthefestival.utility.OnAuctionResponse;
import com.hfad.organizationofthefestival.utility.SimpleServerResponse;
import com.hfad.organizationofthefestival.utility.Specialization;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("festival/{festivalId}/revoke")
    Call<Void> revoke(@Header("Authorization") String authorization,
                      @Path("festivalId") String festivalId);

    @GET("events")
    Call<EventApply[]> getAllEvents(@Query("username") String username,
                                    @Header("Authorization") String authorization);

    @GET("jobs")
    Call<JobApply[]> getAllJobs(@Query("username") String username,
                                @Header("Authorization") String authorization);

    @GET("auctions")
    Call<ApplicationAuction[]> getAuctionedJobs(@Query("username") String username,
                                                @Header("Authorization") String authorization);

    @GET("event/{event_id}")
    Call<EventApply> getEvent(@Path("event_id") String eventId,
                              @Header("Authorization") String authorization);

    @POST("job")
    Call<SimpleServerResponse> createNewJob(@Body NewJob newJob,
                                            @Header("Authorization") String authorization);

    @GET("specializations")
    Call<List<Specialization>> getSpecializations(@Header("Authorization") String authorization);

    @GET("jobs/auction-off")
    Call<Job[]> getNoneAuctionedJobs(@Query("organizer") String username, @Header("Authorization") String authorization);

    @GET("job")
    Call<JobApply> getJobInfo(@Query("job_id") int jobId,
                              @Header("Authorization") String authorization);

    @POST("auction")
    Call<Void> createAuction(@Body HashMap<String, String> body, @Header("Authorization") String authorization);

    @POST("on_auction")
    Call<OnAuctionResponse> checkAuction(@Body HashMap<String, String> body, @Header("Authorization") String authorization);

    @PUT("auction")
    Call<Void> extendAuction(@Body HashMap<String, String> body, @Header("Authorization") String authorization);

    @GET("applications")
    Call<ApplicationResponse[]> getWaitingApplications(@Query("job_id_unconfirmed") int jobId,
                                              @Header("Authorization") String authorization);
    @GET("applications")
    Call<ApplicationResponse[]> getAcceptedApplications(@Query("job_id_confirmed") int jobId,
                                                       @Header("Authorization") String authorization);

    @GET("jobs/event")
    Call<Job[]> getJobsForEvent(@Query("event_id") String eventId,
                                @Header("Authorization") String authorization);

    @PUT("applications")
    Call<Void> setStatus(@Body HashMap<String, String> body,
                         @Header("Authorization") String authorization);
}
