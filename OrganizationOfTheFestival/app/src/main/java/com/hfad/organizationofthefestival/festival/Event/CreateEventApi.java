package com.hfad.organizationofthefestival.festival.Event;

import com.hfad.organizationofthefestival.festival.Festival;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateEventApi {
    @POST("festival")
    Call<CreateEventResponse> createFestival(@Header("Authorization") String authorization,
                                             @Body Festival festival);
}
