package com.hfad.organizationofthefestival.leader;


import com.google.gson.annotations.SerializedName;

public class Application {
    @SerializedName("auction_id")
    private String auctionID;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    private JobApplication job;

    public String getAuctionID() {
        return auctionID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public JobApplication getJob() {
        return job;
    }
}
