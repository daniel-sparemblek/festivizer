package com.hfad.organizationofthefestival.login;

public class LoginResponse {

    private String msg;
    private String access_token;
    private String refresh_token;
    private int permission;

    public LoginResponse(String message, String access_token, String refresh_token, String username, int permission) {
        this.msg = message;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.permission = permission;
    }

    public String getMsg() {
        return msg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getPermission() {
        return permission;
    }
}
