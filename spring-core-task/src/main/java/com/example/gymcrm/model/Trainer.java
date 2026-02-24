package com.example.gymcrm.model;

import java.util.ArrayList;
import java.util.List;

public class Trainer extends User {
    private String specialization;
    private Long id;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}