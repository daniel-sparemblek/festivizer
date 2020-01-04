package com.hfad.organizationofthefestival.event.creation;

import com.hfad.organizationofthefestival.event.Event;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateEventApi {

    @POST("event")
    Call<CreateEventResponse> createEvent(@Header("Authorization") String authorization,
                                          @Body Event event);
}
