package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;
import com.hfad.organizationofthefestival.worker.Specialization;
import com.hfad.organizationofthefestival.worker.Worker;

import java.util.List;

public class JobApply {
    EventApply event;
    Worker worker;
    @SerializedName("start_time")
    String startTime;
    String description;
    List<Specialization> specializations;
    String name;
    @SerializedName("job_id")
    int jobId;
    @SerializedName("is_completed")
    boolean isCompleted;

    public EventApply getEvent() {
        return event;
    }

    public void setEvent(EventApply event) {
        this.event = event;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
