package com.hfad.organizationofthefestival.worker;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface WorkerClient {

    @GET("user/{username}")
    Call<Worker> getWorker(@Path("username") String username, @Header("Autothorization") String authorization);

    @GET("specializations")
    Call<List<Specialization>> getSpecializations(@Header("Autothorization") String authorization);
}
