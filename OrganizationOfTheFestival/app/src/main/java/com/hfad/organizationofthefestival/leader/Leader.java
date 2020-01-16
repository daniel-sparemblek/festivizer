package com.hfad.organizationofthefestival.leader;

import com.google.gson.annotations.SerializedName;
import com.hfad.organizationofthefestival.festival.Festival;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Leader {

    @SerializedName("user_id")
    private int id;
    private String username;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String picture;
    private String phone;
    private String email;
    private int permission;
    @SerializedName("is_pending")
    private int isPending;

    private List<Festival> festivals;

    public Leader(int id, String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission, int isPending) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.permission = permission;
        this.isPending = isPending;
    }

    public int getIsPending() {
        return isPending;
    }

    public void setIsPending(int isPending) {
        this.isPending = isPending;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setPermission(int permission) {
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

    public String getFirstName() {
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

    public int getPermission() {
        return permission;
    }


    public Festival getFestival(String FestivalName){
        Optional<Festival> optionalJob = festivals.stream()
                .filter(t -> t.getName().equals(FestivalName))
                .findAny();
        return optionalJob.orElse(null);
    }

    public List<String> getFestivalNames() {
        return festivals.stream().map(Festival::getName).collect(Collectors.toList());
    }
}
