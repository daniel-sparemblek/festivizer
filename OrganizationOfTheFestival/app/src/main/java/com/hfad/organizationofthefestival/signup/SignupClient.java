package com.hfad.organizationofthefestival.signup;

import com.hfad.organizationofthefestival.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignupClient {

    @POST("registration")
    Call<RegistrationResponse> signup(@Body Signup signup);
}
