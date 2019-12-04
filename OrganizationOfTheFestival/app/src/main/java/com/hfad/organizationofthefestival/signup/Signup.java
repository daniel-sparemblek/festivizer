package com.hfad.organizationofthefestival.signup;

public class Signup {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String picture;
    private String phone;
    private String email;
    private String role;

    public Signup(String username, String password, String firstName, String lastName, String picture, String phone, String email, String role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getInputPassword() {
        return password;
    }

    /*public String getVerifyPassword() {
        return verifyPassword;
    }*/

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

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    /*public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }*/

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
        this.role = role;
    }
}
