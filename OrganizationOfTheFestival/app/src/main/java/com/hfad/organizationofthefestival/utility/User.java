package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String username;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    private String picture;
    private String phone;
    private String email;
    private int permission;
    private String authToken;

    public User(String username, String password, String first_name, String last_name, String picture, String phone, String email, int permission) {
        this.username = username;
        this.password = password;
        this.firstName = first_name;
        this.lastName = last_name;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.permission = permission;
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
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public int getRole() {
        return permission;
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
        this.firstName = first_name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        this.permission = permission;
    }
}
