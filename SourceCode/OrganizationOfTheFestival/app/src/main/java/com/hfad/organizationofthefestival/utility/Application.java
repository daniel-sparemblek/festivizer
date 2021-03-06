package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class Application {
    @SerializedName("job_id")
    private int jobId;
    private double price;
    private String comment;
    private int duration;
    @SerializedName("people_number")
    private int peopleNumber;

    public Application(int jobId, double price, String comment, int duration, int peopleNumber) {
        this.jobId = jobId;
        this.price = price;
        this.comment = comment;
        this.duration = duration;
        this.peopleNumber = peopleNumber;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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
