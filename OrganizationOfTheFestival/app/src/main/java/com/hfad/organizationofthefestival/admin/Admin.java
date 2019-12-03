package com.hfad.organizationofthefestival.admin;

import com.hfad.organizationofthefestival.Role;
import com.hfad.organizationofthefestival.User;

public class Admin extends User {

    public Admin(int userID, String username, String password, String firstName, String lastName, byte[] picture, String phone, String email, Role role) {
        super(userID, username, password, firstName, lastName, picture, phone, email, role);
    }
}
