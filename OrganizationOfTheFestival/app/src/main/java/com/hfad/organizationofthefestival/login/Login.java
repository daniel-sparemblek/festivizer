package com.hfad.organizationofthefestival.login;

public class Login {

    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isValid() {
        if (!username.isEmpty() && !password.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
