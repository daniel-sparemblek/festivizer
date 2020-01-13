package com.hfad.organizationofthefestival.utility;

public enum Role {

    LEADER(1), ORGANIZER(2),  WORKER(3);

    private int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
