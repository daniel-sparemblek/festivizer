package com.hfad.organizationofthefestival.admin;

import com.google.gson.annotations.SerializedName;
import com.hfad.organizationofthefestival.leader.Leader;

import java.util.List;

public class Admin {

    private String username;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("admin_id")
    private int adminId;
    private String picture;
    private String email;
    private int permission;
    private List<Leader> leaders;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    private String phone;

    public Admin(String username, String lastName, int adminId, String picture, String email, int permission, List<Leader> leaders, String password, String firstName, String phone) {
        this.username = username;
        this.lastName = lastName;
        this.adminId = adminId;
        this.picture = picture;
        this.email = email;
        this.permission = permission;
        this.leaders = leaders;
        this.password = password;
        this.firstName = firstName;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public List<Leader> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<Leader> leaders) {
        this.leaders = leaders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
