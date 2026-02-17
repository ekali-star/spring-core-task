package com.example.gymcrm.service;

import com.example.gymcrm.model.User;

import java.security.SecureRandom;
import java.util.Collection;

public class CredentialsGenerator {
    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generateUsername(String firstName,
                                          String lastName,
                                          Collection<? extends User> existingUsers) {

        String base = firstName + "." + lastName;
        String candidate = base;

        int counter = 0;
        boolean exists;

        do {
            exists = false;
            for (User u : existingUsers) {
                if (candidate.equals(u.getUsername())) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                counter++;
                candidate = base + counter;
            }

        } while (exists);

        return candidate;
    }
}
