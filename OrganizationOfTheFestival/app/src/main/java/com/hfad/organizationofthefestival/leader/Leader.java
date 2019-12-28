package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.utility.User;

import java.util.List;

public class Leader extends User {

    public Leader(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }
}
