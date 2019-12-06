package com.hfad.organizationofthefestival.Worker;

import com.hfad.organizationofthefestival.utility.User;

public class Worker extends User {
    public Worker(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }
}
