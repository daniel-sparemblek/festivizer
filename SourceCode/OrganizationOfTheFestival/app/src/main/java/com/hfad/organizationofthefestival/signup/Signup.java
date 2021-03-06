package com.hfad.organizationofthefestival.signup;

import com.google.gson.annotations.SerializedName;

public class Signup {

    private String username;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String picture;
    private String phone;
    private String email;
    private int permission;

    public Signup(String username, String password, String firstName, String lastName, String picture, String phone, String email, int permission) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.permission = permission;
    }

    public String checkInput(String verifyPassword) {
        if (username.equals("")) {
            return "Username cannot be blank!";
        }
        if (!password.equals(verifyPassword)) {
            return "Passwords do not match!";
        }
        if (firstName.equals("")) {
            return "Name cannot be blank!";
        }
        if (lastName.equals("")) {
            return "Last name cannot be blank!";
        }
        if (phone.equals("")) {
            return "Phone cannot be blank!";
        }
        if (email.equals("")) {
            return "email cannot be blank!";
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPicture() {
        return picture;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getRole() {
        return permission;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.permission = permission;
    }
}
