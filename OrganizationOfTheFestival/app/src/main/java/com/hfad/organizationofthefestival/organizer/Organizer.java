package com.hfad.organizationofthefestival.organizer;

import com.hfad.organizationofthefestival.utility.User;

public class Organizer extends User {

    public Organizer(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }
}

