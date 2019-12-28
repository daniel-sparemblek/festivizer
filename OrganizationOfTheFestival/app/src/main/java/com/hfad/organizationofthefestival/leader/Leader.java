package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.utility.User;

public class Leader extends User {

    public Leader (User user){
        super(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getPicture(), user.getPhone(), user.getEmail(), user.getPermission());
    }

    public Leader(String username, String password, String firstName, String lastName,
                  String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }
}
