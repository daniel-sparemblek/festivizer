package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class NewJob {

    private String name;
    private String desc;
    @SerializedName("start_time")
    private String startTime;
    private long event_id;
    @SerializedName("spec1")
    private String specialization1;
    @SerializedName("spec2")
    private String specialization2;
    @SerializedName("spec3")
    private String specialization3;

    public NewJob(String name, String desc, String startTime, long event_id, String specialization1, String specialization2, String specialization3) {
        this.name = name;
        this.desc = desc;
        this.startTime = startTime;
        this.event_id = event_id;
        this.specialization1 = specialization1;
        this.specialization2 = specialization2;
        this.specialization3 = specialization3;
    }
}
