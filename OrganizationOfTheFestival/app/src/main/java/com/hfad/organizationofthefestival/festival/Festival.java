package com.hfad.organizationofthefestival.festival;

public class Festival {

    private long festivalId;
    private long creatorId;
    private String name, desc;
    // private byte[] logo;
    private int duration;
    private boolean active;

    public Festival(long festivalId, long creatorId, String name, String desc, int duration, boolean active) {
        this.festivalId = festivalId;
        this.creatorId = creatorId;
        this.name = name;
        this.desc = desc;
        this.duration = duration;
        this.active = active;
    }

    public long getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(long festivalId) {
        this.festivalId = festivalId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
