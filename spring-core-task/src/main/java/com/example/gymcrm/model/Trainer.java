package com.example.gymcrm.model;

public class Trainer extends User{
    private String specialization;
    private Long userID;
    public String getSpecialization() {return specialization;}
    public Long getUserID() {return userID;}
    public void setSpecialization(String specialization) {this.specialization = specialization;}
    public void setUserID(Long userID) {this.userID = userID;}
}
