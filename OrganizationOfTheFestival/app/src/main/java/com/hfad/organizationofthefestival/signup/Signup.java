package com.hfad.organizationofthefestival.signup;

import com.hfad.organizationofthefestival.Organizer.Organizer;
import com.hfad.organizationofthefestival.Role;
import com.hfad.organizationofthefestival.User;
import com.hfad.organizationofthefestival.Worker;
import com.hfad.organizationofthefestival.leader.Leader;
import java.util.regex.Pattern;

public class Signup {

    private String username;
    private String inputPassword;
    private String verifyPassword;
    private String firstName;
    private String lastName;
    private byte[] picture;
    private String phone;
    private String email;
    private Role role;

    public Signup(String username, String inputPassword, String verifyPassword, String firstName, String lastName, byte[] picture, String phone, String email, Role role) {
        this.username = username;
        this.inputPassword = inputPassword;
        this.verifyPassword = verifyPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public String check(){
        if (username.isEmpty())
            return String.format("username is empty");
        if (email.isEmpty())
            return  String.format("email is empty");
        if (checkEmail()){
            return  String.format("email is not valid");
        }
        if (inputPassword.isEmpty())
            return String.format("password is empty");
        if(verifyPassword.isEmpty())
            return String.format("verify password is empty");
        if (!inputPassword.equals(verifyPassword))
            return String.format("passwords do not match");
        if (phone.isEmpty())
            return String.format("phone is empty");
        if (firstName.isEmpty())
            return String.format("first name is empty");
        if (lastName.isEmpty())
            return String.format("last name is empty");
        return null;
    }

    private boolean checkEmail() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return !pat.matcher(email).matches();
    }

    public User makeUser(){
        switch (role){
            case LEADER:
                return new Leader(username, inputPassword, firstName, lastName, picture, phone, email, role);
            case WORKER:
                return  new Worker(username, inputPassword, firstName, lastName, picture, phone, email, role);
            case ORGANIZER:
                return new Organizer(username, inputPassword, firstName, lastName, picture, phone, email, role);
        }
        return null;
    }
}
