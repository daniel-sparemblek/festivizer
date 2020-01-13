package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class Job {
    @SerializedName("job_id")
    private int id;
    @SerializedName("event_id")
    private int eventId;
    @SerializedName("worker_id")
    private int workerId;
    private String name;
    private String description;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("is_completed")
    private boolean isCompleted;
    private String comment;
    @SerializedName("order_number")
    private String orderNumber;

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
