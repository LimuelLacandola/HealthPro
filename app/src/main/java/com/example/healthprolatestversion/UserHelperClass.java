package com.example.healthprolatestversion;

public class UserHelperClass {

    String username, password, fullname, gender, bloodtype, address, contact;

    public UserHelperClass() {
    }

    public UserHelperClass(String username, String password, String fullname, String gender, String bloodtype, String address, String contact) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.gender = gender;
        this.bloodtype = bloodtype;
        this.address = address;
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
