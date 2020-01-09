package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class NewJob {

    private String name;
    private String desc;
    @SerializedName("start_time")
    private String startTime;
    private String event_id;

    public NewJob(String name, String desc, String startTime, String event_id) {
        this.name = name;
        this.desc = desc;
        this.startTime = startTime;
        this.event_id = event_id;
    }
}
