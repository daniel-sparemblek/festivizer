package com.hfad.organizationofthefestival.utility;

import com.google.gson.annotations.SerializedName;

public class Specialization {

    @SerializedName("specialization_id")
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
