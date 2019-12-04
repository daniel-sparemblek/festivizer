package com.hfad.organizationofthefestival.Organizer;

import com.hfad.organizationofthefestival.Role;
import com.hfad.organizationofthefestival.User;

public class Organizer extends User {

    public Organizer(String username, String password, String firstName, String lastName, byte[] picture, String phone, String email, Role role) {
        super(username, password, firstName, lastName, picture, phone, email, role);
    }
}
