package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.Role;
import com.hfad.organizationofthefestival.User;

public class Leader extends User {
    public Leader(String username, String password, String firstName, String lastName, String picture, String phone, String email, String role) {
        super(username, password, firstName, lastName, picture, phone, email, role);
    }
}
