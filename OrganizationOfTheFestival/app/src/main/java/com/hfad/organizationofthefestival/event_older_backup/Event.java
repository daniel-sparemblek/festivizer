package com.hfad.organizationofthefestival.event_older_backup;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("event_id")
    private long eventId;
    @SerializedName("festival_id")
    private long festivalId;
    @SerializedName("organizer_id")
    private long organizerId;
    private String name;
    private String desc;
    private String location;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;

    public Event(String name, String desc, String location, String startTime, String endTime) {
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return name;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(long festivalId) {
        this.festivalId = festivalId;
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
