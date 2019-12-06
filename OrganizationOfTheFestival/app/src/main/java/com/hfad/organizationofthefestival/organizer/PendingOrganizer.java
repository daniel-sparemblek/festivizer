package com.hfad.organizationofthefestival.organizer;

public class PendingOrganizer {

    private String username;
    private String festivalName;
    private int festivalId;

    public PendingOrganizer(String username, String festivalName, int festivalId) {
        this.username = username;
        this.festivalName = festivalName;
        this.festivalId = festivalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }
}
