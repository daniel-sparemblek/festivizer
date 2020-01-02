package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class Job {
    private int id;
    @SerializedName("event_id")
    private int eventId;
    @SerializedName("worker_id")
    private int workerId;
    private String name;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("is_completed")
    private boolean isCompleted;

    @Override
    public String toString() {
        return name;
    }
}
