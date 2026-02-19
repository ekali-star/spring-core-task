package com.example.gymcrm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Training {
    private Long traineeID;
    private Long trainerID;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;

    public Long getTraineeID() {return traineeID;}
    public Long getTrainerID() {return trainerID;}
    public String getTrainingName() {return trainingName;}
    public String getTrainingType() {return trainingType;}
    public LocalDate getTrainingDate() {return trainingDate;}
    public int getTrainingDuration() {return trainingDuration;}
    public void setTraineeID(Long traineeID) {this.traineeID = traineeID;}
    public void setTrainerID(Long trainerID) {this.trainerID = trainerID;}
    public void setTrainingName(String trainingName) {this.trainingName = trainingName;}
    public void setTrainingType(String trainingType) {this.trainingType = trainingType;}
    public void setTrainingDate(LocalDate trainingDate) {this.trainingDate = trainingDate;}
    public void setTrainingDuration(int trainingDuration) {this.trainingDuration = trainingDuration;}
    public void setTrainerId(long l) {this.trainerID = l;}
    public void setTraineeId(long l) {this.traineeID = l;}

    public long getTraineeId() {return traineeID;}

    public long getTrainerId() {return trainerID;}

    public Object getDate() {return trainingDate;}
}
