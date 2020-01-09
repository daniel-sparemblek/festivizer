package com.hfad.organizationofthefestival.festival.Event;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.utility.WorkingEvent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateEventApi {
    @POST("event")
    Call<CreateEventResponse> createEvent(@Header("Authorization") String authorization,
                                             @Body WorkingEvent event);
}
