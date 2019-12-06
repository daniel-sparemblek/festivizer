package com.hfad.organizationofthefestival.signup;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("msg")
    private String message;

    public RegistrationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
