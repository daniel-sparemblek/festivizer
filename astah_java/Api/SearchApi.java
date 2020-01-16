package com.hfad.organizationofthefestival.search;

import com.hfad.organizationofthefestival.utility.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SearchApi {
    @POST("search/users")
    Call<List<User>> searchUsers(@Body HashMap<String, String> body,
                                 @Header("Authorization") String authorization);
}
