package com.hfad.organizationofthefestival.worker;

import com.hfad.organizationofthefestival.utility.Application;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.SimpleServerResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkerClient {

    @GET("workers")
    Call<Worker> getWorker(@Query("username") String username, @Header("Authorization") String authorization);

    @GET("specializations")
    Call<Specialization[]> getSpecializations(@Header("Authorization") String authorization);

    @POST("specializations")
    Call<Void> createSpecialization(@Body HashMap<String, String> body, @Header("Authorization") String authorization);

    @POST("search/specializations")
    Call<Specialization[]> searchSpecialization(@Body HashMap<String, String> body, @Header("Authorization") String authorization);

    @POST("specializations/{specId}/add")
    Call<Void> addSpecialization(@Path("specId") String specId, @Header("Authorization") String authorization);

    @GET("specializations")
    Call<Specialization[]> getWorkerSpecializations(@Query("username") String username, @Header("Authorization") String authorization);

    @GET("jobs/on_auction")
    Call<Job[]> getJobs(@Header("Authorization") String authorization);

    @GET("jobs")
    Call<JobApply> getJob(@Query("job_id") String jobId, @Header("Authorization") String authorization);

    @POST("applications")
    Call<Void> createApplication(@Body Application application, @Header("Authorization") String authorization);

    @GET("applications")
    Call<ApplicationResponse[]> getWorkerApplications(@Query("username") String username, @Header("Authorization") String authorization);

    @GET("jobs")
    Call<JobApply[]> getActiveJobs(@Query("username") String username, @Query("is_completed") String isCompleted,
                              @Header("Authorization") String authorization);

    @GET("applications")
    Call<Application> getApplication(@Query("application_id") String applicationId, @Header("Authorization") String authorization);

    @PUT("jobs/{job_id}")
    Call<SimpleServerResponse> addComment(@Path("job_id") String jobId,
                                          @Body Map<String, String> comment,
                                          @Header("Authorization") String authorization);
}
