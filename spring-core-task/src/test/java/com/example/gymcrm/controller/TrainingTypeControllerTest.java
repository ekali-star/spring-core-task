package com.example.gymcrm.controller;

import com.example.gymcrm.dto.response.TrainingTypeResponse;
import com.example.gymcrm.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private GymFacade facade;

    private TrainingTypeController controller;

    @BeforeEach
    void setUp() {
        controller = new TrainingTypeController(facade);
    }

    @Test
    void getAll_ShouldReturnList() {
        List<TrainingTypeResponse> expected = List.of(
                new TrainingTypeResponse(1L, "Cardio"),
                new TrainingTypeResponse(2L, "Strength"),
                new TrainingTypeResponse(3L, "Yoga")
        );
        when(facade.getAllTrainingTypes()).thenReturn(expected);

        List<TrainingTypeResponse> result = controller.getAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Cardio", result.get(0).getName());
        verify(facade).getAllTrainingTypes();
    }

    @Test
    void getAll_ShouldReturnEmptyList() {
        when(facade.getAllTrainingTypes()).thenReturn(List.of());

        List<TrainingTypeResponse> result = controller.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facade).getAllTrainingTypes();
    }
}