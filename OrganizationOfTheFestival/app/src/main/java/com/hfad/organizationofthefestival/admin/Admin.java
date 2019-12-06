package com.hfad.organizationofthefestival.admin;

import com.hfad.organizationofthefestival.utility.User;
import com.hfad.organizationofthefestival.leader.Leader;

import java.util.LinkedList;

public class Admin extends User {

    LinkedList<Leader> leaders;

    public Admin(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        super(username, password, firstName, lastName, picture, phone, email, permission);
    }
}
