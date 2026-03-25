package com.example.gymcrm.facade;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.dto.request.*;
import com.example.gymcrm.dto.response.*;
import com.example.gymcrm.model.*;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    private GymFacade facade;

    private User traineeUser;
    private Trainee trainee;
    private User trainerUser;
    private Trainer trainer;
    private Training training;
    private TrainingType trainingType;
    private AuthCredentials authCredentials;

    @BeforeEach
    void setUp() {
        facade = new GymFacade(traineeService, trainerService, trainingService);

        traineeUser = User.builder()
                .id(1L)
                .username("john.doe")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .isActive(true)
                .build();

        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(traineeUser);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Main St");
        trainee.setTrainers(new ArrayList<>());

        trainerUser = User.builder()
                .id(2L)
                .username("mike.smith")
                .firstName("Mike")
                .lastName("Smith")
                .password("password")
                .isActive(true)
                .build();

        trainingType = new TrainingType(1L, "Cardio");

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(trainerUser);
        trainer.setSpecialization(trainingType);
        trainer.setTrainees(new ArrayList<>());

        training = Training.builder()
                .id(1L)
                .trainingName("Morning Run")
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .trainingType(trainingType)
                .trainee(trainee)
                .trainer(trainer)
                .build();

        authCredentials = new AuthCredentials("john.doe", "password");
    }

    @Test
    void login_ShouldSucceedWhenTraineeAuthenticates() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);

        assertDoesNotThrow(() -> facade.login(new LoginRequest("john.doe", "password")));
        verify(traineeService).authenticate("john.doe", "password");
        verify(trainerService, never()).authenticate(anyString(), anyString());
    }

    @Test
    void login_ShouldSucceedWhenTrainerAuthenticates() {
        when(traineeService.authenticate("mike.smith", "password")).thenReturn(false);
        when(trainerService.authenticate("mike.smith", "password")).thenReturn(true);

        assertDoesNotThrow(() -> facade.login(new LoginRequest("mike.smith", "password")));
        verify(traineeService).authenticate("mike.smith", "password");
        verify(trainerService).authenticate("mike.smith", "password");
    }

    @Test
    void login_ShouldThrowWhenAuthenticationFails() {
        when(traineeService.authenticate("unknown", "wrong")).thenReturn(false);
        when(trainerService.authenticate("unknown", "wrong")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> facade.login(new LoginRequest("unknown", "wrong")));
    }

    @Test
    void changePassword_ShouldChangeTraineePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("john.doe", "oldPass", "newPass");
        when(traineeService.authenticate(any(Auth.class))).thenReturn(true);

        assertDoesNotThrow(() -> facade.changePassword(request));
        verify(traineeService).changePassword(any(Auth.class), eq("newPass"));
        verify(trainerService, never()).changePassword(any(), any());
    }

    @Test
    void changePassword_ShouldChangeTrainerPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("mike.smith", "oldPass", "newPass");
        when(traineeService.authenticate(any(Auth.class))).thenReturn(false);
        when(trainerService.authenticate(any(Auth.class))).thenReturn(true);

        assertDoesNotThrow(() -> facade.changePassword(request));
        verify(trainerService).changePassword(any(Auth.class), eq("newPass"));
        verify(traineeService, never()).changePassword(any(), any());
    }

    @Test
    void changePassword_ShouldThrowWhenAuthenticationFails() {
        ChangePasswordRequest request = new ChangePasswordRequest("unknown", "oldPass", "newPass");
        when(traineeService.authenticate(any(Auth.class))).thenReturn(false);
        when(trainerService.authenticate(any(Auth.class))).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> facade.changePassword(request));
    }

    @Test
    void createTrainee_ShouldReturnAuthCredentials() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setAddress("123 Main St");

        AuthCredentials expected = new AuthCredentials("john.doe", "generatedPass");
        when(traineeService.create(any(Trainee.class))).thenReturn(expected);

        AuthCredentials result = facade.createTrainee(request);

        assertEquals(expected, result);
        verify(traineeService).create(any(Trainee.class));
    }

    @Test
    void getTraineeByUsername_ShouldReturnTraineeProfile() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);
        when(traineeService.findByUsername("john.doe")).thenReturn(trainee);

        TraineeProfileResponse result = facade.getTraineeByUsername(authCredentials, "john.doe");

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        assertEquals("123 Main St", result.getAddress());
        assertTrue(result.getIsActive());
        assertNotNull(result.getTrainers());
    }

    @Test
    void updateTrainee_ShouldReturnUpdatedProfile() {
        UpdateTraineeRequest request = new UpdateTraineeRequest();
        request.setUsername("john.doe");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(1991, 2, 2));
        request.setAddress("456 Oak St");

        when(traineeService.updateTrainee(any(Auth.class), eq("john.doe"), any(Trainee.class)))
                .thenReturn(trainee);

        TraineeProfileResponse result = facade.updateTrainee(authCredentials, "john.doe", request);

        assertNotNull(result);
        verify(traineeService).updateTrainee(any(Auth.class), eq("john.doe"), any(Trainee.class));
    }

    @Test
    void deleteTrainee_ShouldCallService() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);

        assertDoesNotThrow(() -> facade.deleteTrainee(authCredentials, "john.doe"));
        verify(traineeService).deleteTrainee("john.doe");
    }

    @Test
    void setTraineeActiveStatus_ShouldCallService() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);

        facade.setTraineeActiveStatus(authCredentials, "john.doe", false);

        verify(traineeService).setActiveStatus("john.doe", false);
    }

    @Test
    void getUnassignedTrainers_ShouldReturnList() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);
        when(traineeService.getUnassignedTrainers("john.doe")).thenReturn(List.of(trainer));

        List<TrainerSummaryDTO> result = facade.getUnassignedTrainers(authCredentials, "john.doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("mike.smith", result.get(0).getUsername());
        assertEquals("Mike", result.get(0).getFirstName());
        assertEquals("Smith", result.get(0).getLastName());
        assertEquals("Cardio", result.get(0).getSpecialization());
    }

    @Test
    void updateTraineeTrainers_ShouldReturnUpdatedTrainers() {
        List<String> trainerUsernames = List.of("trainer1", "trainer2");

        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);
        doNothing().when(traineeService).updateTrainers("john.doe", trainerUsernames);
        when(traineeService.findByUsername("john.doe")).thenReturn(trainee);

        List<TrainerSummaryDTO> result = facade.updateTraineeTrainers(authCredentials, "john.doe", trainerUsernames);

        assertNotNull(result);
        verify(traineeService).updateTrainers("john.doe", trainerUsernames);
    }

    @Test
    void getTraineeTrainings_ShouldReturnList() {
        when(traineeService.authenticate("john.doe", "password")).thenReturn(true);
        when(trainingService.getTraineeTrainings(eq("john.doe"), any(), any(), any(), any()))
                .thenReturn(List.of(training));

        List<TrainingResponse> result = facade.getTraineeTrainings(
                authCredentials, "john.doe", null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morning Run", result.get(0).getTrainingName());
        assertEquals("mike.smith", result.get(0).getPartnerName());
    }

    @Test
    void createTrainer_ShouldReturnAuthCredentials() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName("Mike");
        request.setLastName("Smith");
        request.setSpecializationId(1L);

        AuthCredentials expected = new AuthCredentials("mike.smith", "generatedPass");
        when(trainerService.create(any(Trainer.class))).thenReturn(expected);

        AuthCredentials result = facade.createTrainer(request);

        assertEquals(expected, result);
        verify(trainerService).create(any(Trainer.class));
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainerProfile() {
        when(trainerService.authenticate("mike.smith", "password")).thenReturn(true);
        when(trainerService.findByUsername("mike.smith")).thenReturn(trainer);

        AuthCredentials auth = new AuthCredentials("mike.smith", "password");
        TrainerProfileResponse result = facade.getTrainerByUsername(auth, "mike.smith");

        assertNotNull(result);
        assertEquals("mike.smith", result.getUsername());
        assertEquals("Mike", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("Cardio", result.getSpecialization());
        assertTrue(result.getIsActive());
        assertNotNull(result.getTrainees());
    }

    @Test
    void updateTrainer_ShouldReturnUpdatedProfile() {
        UpdateTrainerRequest request = new UpdateTrainerRequest();
        request.setUsername("mike.smith");
        request.setFirstName("Michael");
        request.setLastName("Johnson");

        when(trainerService.updateTrainer(any(Auth.class), eq("mike.smith"), any(Trainer.class)))
                .thenReturn(trainer);

        AuthCredentials auth = new AuthCredentials("mike.smith", "password");
        TrainerProfileResponse result = facade.updateTrainer(auth, "mike.smith", request);

        assertNotNull(result);
        verify(trainerService).updateTrainer(any(Auth.class), eq("mike.smith"), any(Trainer.class));
    }

    @Test
    void setTrainerActiveStatus_ShouldCallService() {
        when(trainerService.authenticate("mike.smith", "password")).thenReturn(true);

        AuthCredentials auth = new AuthCredentials("mike.smith", "password");
        facade.setTrainerActiveStatus(auth, "mike.smith", false);

        verify(trainerService).setActiveStatus("mike.smith", false);
    }

    @Test
    void getTrainerTrainings_ShouldReturnList() {
        when(trainerService.authenticate("mike.smith", "password")).thenReturn(true);
        when(trainingService.getTrainerTrainings(eq("mike.smith"), any(), any(), any()))
                .thenReturn(List.of(training));

        AuthCredentials auth = new AuthCredentials("mike.smith", "password");
        List<TrainingResponse> result = facade.getTrainerTrainings(
                auth, "mike.smith", null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morning Run", result.get(0).getTrainingName());
        assertEquals("john.doe", result.get(0).getPartnerName());
    }

    @Test
    void createTraining_ShouldCallService() {
        AddTrainingRequest request = new AddTrainingRequest();
        request.setTraineeUsername("john.doe");
        request.setTrainerUsername("mike.smith");
        request.setTrainingName("Evening Run");
        request.setTrainingDate(LocalDate.now());
        request.setTrainingDuration(45);

        facade.createTraining(request);

        verify(trainingService).createTraining(eq("john.doe"), eq("mike.smith"), any(Training.class));
    }

    @Test
    void getAllTrainingTypes_ShouldReturnList() {
        when(trainingService.findAll()).thenReturn(List.of(training));

        List<TrainingTypeResponse> result = facade.getAllTrainingTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Cardio", result.get(0).getName());
    }
}