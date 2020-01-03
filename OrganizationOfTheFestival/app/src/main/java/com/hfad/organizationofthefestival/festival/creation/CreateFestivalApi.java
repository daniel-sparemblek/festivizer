package com.hfad.organizationofthefestival.festival.creation;

import com.hfad.organizationofthefestival.festival.Festival;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateFestivalApi {
    @POST("festival")
    Call<CreateFestivalResponse> createFestival(@Header("Authorization") String authorization,
                                                @Body Festival festival);
}
