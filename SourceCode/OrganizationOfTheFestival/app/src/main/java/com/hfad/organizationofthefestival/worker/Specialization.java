package com.hfad.organizationofthefestival.worker;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Specialization {

    @SerializedName("specialization_id")
    private int specializationId;

    private String name;

    public Specialization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialization that = (Specialization) o;
        return specializationId == that.specializationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(specializationId);
    }
}
