package com.hfad.organizationofthefestival.leader;

import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.utility.User;

import java.util.List;

public class Leader extends User {

    private List<Festival> festivals;

    public Leader(User user){
        this(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getPicture(), user.getPhone(), user.getEmail(), user.getPermission());
    }

    public Leader(String username, String password, String firstName, String lastName,
                  String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }
}
