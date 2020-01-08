package com.hfad.organizationofthefestival.leader;


import com.google.gson.annotations.SerializedName;

public class Auction {
    @SerializedName("auction_id")
    private String auctionID;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    private JobAuction job;

    public String getAuctionID() {
        return auctionID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public JobAuction getJob() {
        return job;
    }
}
