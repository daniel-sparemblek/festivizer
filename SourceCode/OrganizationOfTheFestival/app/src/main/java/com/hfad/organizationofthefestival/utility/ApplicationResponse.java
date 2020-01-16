package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class ApplicationResponse {
    @SerializedName("application_id")
    private int applicationId;
    private ApplicationAuction auction;
    private double price;
    private String comment;
    private int duration;
    private User worker;
    @SerializedName("people_number")
    private int peopleNumber;
    private int status;

    public ApplicationResponse(int applicationId, ApplicationAuction auction, double price, String comment, int duration, User worker, int peopleNumber) {
        this.applicationId = applicationId;
        this.auction = auction;
        this.price = price;
        this.comment = comment;
        this.duration = duration;
        this.worker = worker;
        this.peopleNumber = peopleNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationAuction getAuction() {
        return auction;
    }

    public void setAuction(ApplicationAuction auction) {
        this.auction = auction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }
}
