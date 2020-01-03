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
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    private int status;

    public long getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(long festivalId) {
        this.festivalId = festivalId;
    }

    public long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(long leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStartTime() {
        return startTime;
    }


    public String getEndTime() {
        return endTime;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Festival(String name, String desc, String logo, String startTime, String endTime) {
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
