package com.hfad.organizationofthefestival.worker;

import com.google.gson.annotations.SerializedName;
import com.hfad.organizationofthefestival.utility.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Worker {

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
    private List<Specialization> specializations;
    private List<Job> jobs;

    public Worker(int id, String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission, List<Specialization> specializations) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.permission = permission;
        this.specializations = specializations;
    }

    public List<String> getJobNames() {
        return jobs.stream()
                .map(Job::getName)
                .collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public Job getJob(String jobName){
        Optional<Job> optionalJob = jobs.stream()
                .filter(t -> t.getName().equals(jobName))
                .findAny();
        return optionalJob.orElse(null);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<String> getSpecializations() {
        return specializations.stream()
                .map(Specialization::getName)
                .collect(Collectors.toList());
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }
}
