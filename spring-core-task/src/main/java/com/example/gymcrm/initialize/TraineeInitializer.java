package com.example.gymcrm.initialize;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.storage.TraineeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TraineeInitializer implements StoreInitializer {

    private static final Logger log = LoggerFactory.getLogger(TraineeInitializer.class);
    private final TraineeStorage traineeStorage;

    public TraineeInitializer(TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public String scopeName() {
        return "TRAINEE";
    }

    @Override
    public void parseLineAndSave(String line) {
        String[] parts = line.split(",");
        if (parts.length < 8) {
            log.warn("Skipping invalid trainee line: {}", line);
            return;
        }

        try {
            Trainee trainee = new Trainee();
            trainee.setId(Long.parseLong(parts[0].trim()));
            trainee.setFirstName(parts[1].trim());
            trainee.setLastName(parts[2].trim());
            trainee.setUsername(parts[3].trim());
            trainee.setPassword(parts[4].trim());
            trainee.setActive(Boolean.parseBoolean(parts[5].trim()));
            trainee.setDateOfBirth(LocalDate.parse(parts[6].trim(), DateTimeFormatter.ISO_LOCAL_DATE));
            trainee.setAddress(parts[7].trim());

            traineeStorage.getStorage().put(trainee.getId(), trainee);
            log.debug("Loaded trainee: {}", trainee.getUsername());
        } catch (Exception e) {
            log.error("Failed to parse trainee line: {}", line, e);
        }
    }
}