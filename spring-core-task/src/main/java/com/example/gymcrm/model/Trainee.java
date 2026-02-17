package com.example.gymcrm.model;

import java.time.LocalDate;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private Long userID;
    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public String getAddress() {return address;}
    public Long getUserID() {return userID;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public void setAddress(String address) {this.address = address;}
    public void setUserID(Long userID) {this.userID = userID;}
}
