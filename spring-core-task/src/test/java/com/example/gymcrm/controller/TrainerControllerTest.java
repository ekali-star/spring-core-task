package com.example.gymcrm.controller;

import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.TrainerProfileResponse;
import com.example.gymcrm.dto.response.TrainingResponse;
import com.example.gymcrm.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private GymFacade facade;

    private TrainerController controller;

    private TrainerRegistrationRequest registrationRequest;
    private UpdateTrainerRequest updateRequest;
    private ActivateRequest activateRequest;
    private TrainerTrainingQueryRequest trainingQueryRequest;
    private TrainerProfileResponse profileResponse;
    private List<TrainingResponse> trainingResponses;

    @BeforeEach
    void setUp() {
        controller = new TrainerController(facade);

        registrationRequest = new TrainerRegistrationRequest();
        registrationRequest.setFirstName("Mike");
        registrationRequest.setLastName("Smith");
        registrationRequest.setSpecializationId(1L);

        updateRequest = new UpdateTrainerRequest();
        updateRequest.setUsername("mike.smith");
        updateRequest.setFirstName("Michael");
        updateRequest.setLastName("Johnson");

        activateRequest = new ActivateRequest();
        activateRequest.setUsername("mike.smith");
        activateRequest.setIsActive(true);

        trainingQueryRequest = new TrainerTrainingQueryRequest();
        trainingQueryRequest.setPeriodFrom(LocalDate.of(2024, 1, 1));
        trainingQueryRequest.setPeriodTo(LocalDate.of(2024, 12, 31));
        trainingQueryRequest.setTraineeName("John");

        profileResponse = new TrainerProfileResponse(
                "mike.smith", "Mike", "Smith",
                "Cardio", true, List.of()
        );

        trainingResponses = List.of(
                new TrainingResponse("Morning Run", LocalDate.now(), "Cardio", 60, "john.doe")
        );
    }

    @Test
    void register_ShouldReturnAuthCredentials() {
        AuthCredentials expected = new AuthCredentials("mike.smith", "password");
        when(facade.createTrainer(registrationRequest)).thenReturn(expected);

        AuthCredentials result = controller.register(registrationRequest);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(facade).createTrainer(registrationRequest);
    }

    @Test
    void get_ShouldReturnTrainerProfile() {
        when(facade.getTrainerByUsername(any(AuthCredentials.class), eq("mike.smith")))
                .thenReturn(profileResponse);

        TrainerProfileResponse result = controller.get("mike.smith", "mike.smith", "password");

        assertNotNull(result);
        assertEquals("mike.smith", result.getUsername());
        assertEquals("Mike", result.getFirstName());
        verify(facade).getTrainerByUsername(any(AuthCredentials.class), eq("mike.smith"));
    }

    @Test
    void update_ShouldReturnUpdatedProfile() {
        when(facade.updateTrainer(any(AuthCredentials.class), eq("mike.smith"), eq(updateRequest)))
                .thenReturn(profileResponse);

        TrainerProfileResponse result = controller.update(updateRequest, "mike.smith", "password");

        assertNotNull(result);
        verify(facade).updateTrainer(any(AuthCredentials.class), eq("mike.smith"), eq(updateRequest));
    }

    @Test
    void activate_ShouldCallFacade() {
        controller.activate(activateRequest, "mike.smith", "password");

        verify(facade).setTrainerActiveStatus(any(AuthCredentials.class), eq("mike.smith"), eq(true));
    }

    @Test
    void getTrainings_ShouldReturnList() {
        when(facade.getTrainerTrainings(
                any(AuthCredentials.class), eq("mike.smith"),
                eq(trainingQueryRequest.getPeriodFrom()),
                eq(trainingQueryRequest.getPeriodTo()),
                eq(trainingQueryRequest.getTraineeName())))
                .thenReturn(trainingResponses);

        List<TrainingResponse> result = controller.getTrainings(
                "mike.smith", trainingQueryRequest, "mike.smith", "password");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(facade).getTrainerTrainings(
                any(AuthCredentials.class), eq("mike.smith"),
                eq(trainingQueryRequest.getPeriodFrom()),
                eq(trainingQueryRequest.getPeriodTo()),
                eq(trainingQueryRequest.getTraineeName()));
    }

    @Test
    void getTrainings_ShouldHandleNullQueryParameters() {
        TrainerTrainingQueryRequest emptyQuery = new TrainerTrainingQueryRequest();

        when(facade.getTrainerTrainings(
                any(AuthCredentials.class), eq("mike.smith"),
                isNull(), isNull(), isNull()))
                .thenReturn(trainingResponses);

        List<TrainingResponse> result = controller.getTrainings(
                "mike.smith", emptyQuery, "mike.smith", "password");

        assertNotNull(result);
        verify(facade).getTrainerTrainings(
                any(AuthCredentials.class), eq("mike.smith"),
                isNull(), isNull(), isNull());
    }
}