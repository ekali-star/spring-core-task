package com.example.gymcrm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private Long id;
    private List<Training> trainings = new ArrayList<>();

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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