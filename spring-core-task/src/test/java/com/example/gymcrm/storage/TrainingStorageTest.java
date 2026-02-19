package com.example.gymcrm.storage;

import com.example.gymcrm.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingStorageTest {

    private TrainingStorage trainingStorage;

    @BeforeEach
    void setUp() {
        trainingStorage = new TrainingStorage();
    }

    @Test
    void getStorage_ShouldReturnEmptyMap_WhenNoTrainingsAdded() {
        Map<Long, Training> storage = trainingStorage.getStorage();
        assertNotNull(storage);
        assertTrue(storage.isEmpty());
    }

    @Test
    void getStorage_ShouldReturnMapWithTrainings_WhenTrainingsAdded() {
        Map<Long, Training> storage = trainingStorage.getStorage();
        Training training = new Training();
        training.setTraineeId(1L);
        training.setTrainerId(1L);
        training.setTrainingName("Yoga Class");
        training.setTrainingDate(LocalDate.now());
        storage.put(1L, training);
        assertEquals(1, storage.size());
        assertEquals(training, storage.get(1L));
    }

    @Test
    void getStorage_ShouldReturnSameInstance_WhenCalledMultipleTimes() {
        Map<Long, Training> storage1 = trainingStorage.getStorage();
        Map<Long, Training> storage2 = trainingStorage.getStorage();
        assertSame(storage1, storage2);
    }
}