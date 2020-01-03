package com.hfad.organizationofthefestival.worker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WorkerClient {

    @GET("workers")
    Call<Worker> getWorker(@Query("username") String username, @Header("Authorization") String authorization);

    @GET("specializations")
    Call<Specialization[]> getSpecializations(@Header("Authorization") String authorization);
}
