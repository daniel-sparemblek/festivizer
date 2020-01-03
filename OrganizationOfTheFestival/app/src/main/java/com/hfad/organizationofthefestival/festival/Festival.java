package com.hfad.organizationofthefestival.festival;

import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;

public class Festival {

    @SerializedName("festival_id")
    private long festivalId;
    @SerializedName("leader_id")
    private long leaderId;
    private String name;
    private String desc;
    private String logo;
    @SerializedName("start_time")
    private ZonedDateTime startTime;
    @SerializedName("end_time")
    private ZonedDateTime endTime;
    private int status;

    public Festival(String name, String desc, String logo, ZonedDateTime startTime, ZonedDateTime endTime) {
        this.name = name;
        this.desc = desc;
        this.logo = logo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
