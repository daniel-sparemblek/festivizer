package com.hfad.organizationofthefestival.signup;

public class RegistrationResponse {

    private String message;
    private String access_token;
    private String refresh_token;

    public RegistrationResponse(String message, String access_token, String refresh_token) {
        this.message = message;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public String getMessage() {
        return message;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
