package com.example.gymcrm.initialize;

import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.storage.TrainingStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TrainingInitializer implements StoreInitializer {

    private static final Logger log = LoggerFactory.getLogger(TrainingInitializer.class);
    private final TrainingStorage trainingStorage;

    public TrainingInitializer(TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public String scopeName() {
        return "TRAINING";
    }

    @Override
    public void parseLineAndSave(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            log.warn("Skipping invalid training line: {}", line);
            return;
        }

        try {
            Training training = new Training();
            training.setId(Long.parseLong(parts[0].trim()));
            training.setTraineeId(Long.parseLong(parts[1].trim()));
            training.setTrainerId(Long.parseLong(parts[2].trim()));
            training.setTrainingName(parts[3].trim());
            training.setTrainingType(TrainingType.fromString(parts[4].trim()));
            training.setTrainingDate(LocalDate.parse(parts[5].trim(), DateTimeFormatter.ISO_LOCAL_DATE));
            training.setTrainingDuration(Integer.parseInt(parts[6].trim()));

            trainingStorage.getStorage().put(training.getId(), training);
            log.debug("Loaded training: {}", training.getTrainingName());
        } catch (Exception e) {
            log.error("Failed to parse training line: {}", line, e);
        }
    }
}