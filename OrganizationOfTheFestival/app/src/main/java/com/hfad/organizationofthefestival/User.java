package com.hfad.organizationofthefestival;

public class User {

    private int id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String picture;
    private String phone;
    private String email;
    private String role;
    private String authToken;

    public User(String username, String password, String first_name, String last_name, String picture, String phone, String email, String role) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPicture() {
        return picture;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
