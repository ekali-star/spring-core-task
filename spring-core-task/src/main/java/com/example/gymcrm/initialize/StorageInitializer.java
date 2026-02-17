package com.example.gymcrm.initialize;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.storage.TraineeStorage;
import com.example.gymcrm.storage.TrainerStorage;
import com.example.gymcrm.storage.TrainingStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

@Component
public class StorageInitializer implements BeanPostProcessor {

    @Value("${storage.init.file}")
    private String path;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof TraineeStorage traineeStorage) {
            initTrainees(traineeStorage);
        }

        if (bean instanceof TrainerStorage trainerStorage) {
            initTrainers(trainerStorage);
        }

        if (bean instanceof TrainingStorage trainingStorage) {
            initTrainings(trainingStorage);
        }

        return bean;
    }

    private void initTrainees(TraineeStorage storage) {
        Trainee t1 = new Trainee();
        t1.setUserId(1L);
        t1.setFirstName("John");
        t1.setLastName("Doe");
        t1.setActive(true);

        storage.getStorage().put(t1.getUserId(), t1);
    }

    private void initTrainers(TrainerStorage storage) {
        Trainer t1 = new Trainer();
        t1.setUserId(1L);
        t1.setFirstName("Jane");
        t1.setLastName("Smith");
        t1.setSpecialization("Fitness");
        t1.setActive(true);

        storage.getStorage().put(t1.getUserId(), t1);
    }

    private void initTrainings(TrainingStorage storage) {
        Training training = new Training();
        training.setTrainerId(1L);
        training.setTraineeId(1L);
        training.setTrainingName("Morning Cardio");
        training.setTrainingType("Cardio");
        training.setTrainingDuration(60);

        storage.getStorage().put(1L, training);
    }
}
