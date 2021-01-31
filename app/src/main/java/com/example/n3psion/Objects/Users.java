package com.example.n3psion.Objects;

public class Users {
    private String userID;
    private String userName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String Dob;
    private String gender;


    public Users(){
    }

    public Users(String userID, String userName, String lastName, String email, String password, String phone, String dob, String Gender) {
        this.userID = userID;
        this.userName = userName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.Dob = dob;
        this.gender = Gender;

    }

    public String getDob() {
        return Dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setDob(String DOB){
        this.Dob = DOB;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
