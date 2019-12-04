package com.hfad.organizationofthefestival;

import android.os.Parcel;

public class Worker extends User {
    public Worker(String username, String password, String firstName, String lastName, byte[] picture, String phone, String email, Role role) {
        super(username, password, firstName, lastName, picture, phone, email, role);
    }
}
