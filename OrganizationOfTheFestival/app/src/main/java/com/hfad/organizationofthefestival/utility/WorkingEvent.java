package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class WorkingEvent {
    @SerializedName("festival_id")
    private int festivalId;
    @SerializedName("organizer_id")
    private int organizerId;
    private String name;
    private String desc;
    private String location;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;

    public WorkingEvent(int festivalId, int organizerId, String name, String desc, String location, String startTime, String endTime) {
        this.festivalId = festivalId;
        this.organizerId = organizerId;
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
