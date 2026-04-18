package com.example.gymcrm.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MS = 5 * 60 * 1000L;

    private record AttemptInfo(int count, Instant blockedUntil) {
    }

    private final ConcurrentHashMap<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public void loginFailed(String username) {
        attempts.merge(username,
                new AttemptInfo(1, null),
                (existing, ignored) -> {
                    int newCount = existing.count() + 1;
                    Instant blocked = newCount >= MAX_ATTEMPTS
                            ? Instant.now().plusMillis(BLOCK_DURATION_MS)
                            : existing.blockedUntil();
                    return new AttemptInfo(newCount, blocked);
                });
    }

    public boolean isBlocked(String username) {
        AttemptInfo info = attempts.get(username);
        if (info == null || info.blockedUntil() == null) return false;
        if (Instant.now().isAfter(info.blockedUntil())) {
            attempts.remove(username);
            return false;
        }
        return true;
    }
}