package com.example.gymcrm.model;

import java.util.ArrayList;
import java.util.List;

public class Trainer extends User {
    private String specialization;
    private Long id;
    private List<Training> trainings = new ArrayList<>();

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

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public void addTraining(Training training) {
        trainings.add(training);
    }
}