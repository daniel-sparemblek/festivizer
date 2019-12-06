package com.hfad.organizationofthefestival.worker;

import com.hfad.organizationofthefestival.utility.User;

import java.util.ArrayList;
import java.util.List;

public class Worker extends User {

    private List<Specialization> specializations;

    public Worker(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
        specializations = new ArrayList<>();
    }
}
