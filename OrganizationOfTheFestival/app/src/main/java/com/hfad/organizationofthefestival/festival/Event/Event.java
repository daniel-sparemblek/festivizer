package com.hfad.organizationofthefestival.festival.Event;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("festival_id")
    private long festivalId;
    @SerializedName("event_id")
    private long eventId;
    @SerializedName("organizer_id")
    private long organizerId;
    private String name;
    private String desc;
    private String location;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    private int status;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(long organizerId) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Event(String name, String desc, String location, String startTime, String endTime) {
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
