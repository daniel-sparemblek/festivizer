package com.hfad.organizationofthefestival.signup;

import com.hfad.organizationofthefestival.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SignupClient {

    @POST("registration")
    Call<RegistrationResponse> signup(@Body Signup signup);

    @GET("registration")
    Call<User> getUser(@Header("Authorization") String authToken);


}
