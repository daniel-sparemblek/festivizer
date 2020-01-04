package com.hfad.organizationofthefestival.worker;

import com.hfad.organizationofthefestival.utility.Job;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("festival/{festivalId}")
    Call<Job> getJob(@Path("festivalId") String festivalId, @Header("Authorization") String authorization);
}
