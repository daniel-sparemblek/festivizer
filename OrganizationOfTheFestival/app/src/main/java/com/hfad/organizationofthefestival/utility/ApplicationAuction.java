package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class ApplicationAuction {
    @SerializedName("auction_id")
    private int auctionId;
    private Job job;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;

    public ApplicationAuction(int auctionId, Job job, String startTime, String endTime) {
        this.auctionId = auctionId;
        this.job = job;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
