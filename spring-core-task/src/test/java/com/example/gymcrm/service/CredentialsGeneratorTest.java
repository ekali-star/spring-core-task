package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsGeneratorTest {

    @Test
    void generatePassword_ShouldReturn10CharString() {
        String password = CredentialsGenerator.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generatePassword_ShouldBeRandom() {
        String password1 = CredentialsGenerator.generatePassword();
        String password2 = CredentialsGenerator.generatePassword();

        assertNotEquals(password1, password2);
    }

    @Test
    void generateUsername_ShouldCreateBaseUsername_WhenNoDuplicates() {
        String username = CredentialsGenerator.generateUsername("John", "Doe", Collections.emptyList());

        assertEquals("John.Doe", username);
    }

    @Test
    void generateUsername_ShouldAddNumber_WhenDuplicateExists() {
        Trainee existing = new Trainee();
        existing.setUsername("John.Doe");
        Collection<User> existingUsers = List.of(existing);

        String username = CredentialsGenerator.generateUsername("John", "Doe", existingUsers);

        assertEquals("John.Doe1", username);
    }

    @Test
    void generateUsername_ShouldHandleMultipleDuplicates() {
        Trainee existing1 = new Trainee();
        existing1.setUsername("John.Doe");
        Trainee existing2 = new Trainee();
        existing2.setUsername("John.Doe1");
        Collection<User> existingUsers = Arrays.asList(existing1, existing2);

        String username = CredentialsGenerator.generateUsername("John", "Doe", existingUsers);

        assertEquals("John.Doe2", username);
    }

    @Test
    void generateUsername_ShouldBeCaseSensitive() {
        Trainee existing = new Trainee();
        existing.setUsername("john.doe");
        Collection<User> existingUsers = List.of(existing);

        String username = CredentialsGenerator.generateUsername("John", "Doe", existingUsers);

        assertEquals("John.Doe", username);
    }
}