package com.example.gymcrm.initialize;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.storage.TrainerStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainerInitializer implements StoreInitializer {

    private static final Logger log = LoggerFactory.getLogger(TrainerInitializer.class);
    private final TrainerStorage trainerStorage;

    public TrainerInitializer(TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public String scopeName() {
        return "TRAINER";
    }

    @Override
    public void parseLineAndSave(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            log.warn("Skipping invalid trainer line: {}", line);
            return;
        }

        try {
            Trainer trainer = new Trainer();
            trainer.setId(Long.parseLong(parts[0].trim()));
            trainer.setFirstName(parts[1].trim());
            trainer.setLastName(parts[2].trim());
            trainer.setUsername(parts[3].trim());
            trainer.setPassword(parts[4].trim());
            trainer.setActive(Boolean.parseBoolean(parts[5].trim()));
            trainer.setSpecialization(parts[6].trim());

            trainerStorage.getStorage().put(trainer.getId(), trainer);
            log.debug("Loaded trainer: {}", trainer.getUsername());
        } catch (Exception e) {
            log.error("Failed to parse trainer line: {}", line, e);
        }
    }
}