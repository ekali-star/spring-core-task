package com.example.gymcrm.service;

import com.example.gymcrm.dto.Auth;
import com.example.gymcrm.dto.AuthCredentials;
import com.example.gymcrm.model.User;
import com.example.gymcrm.model.UserComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public abstract class UserService<T extends UserComparable> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected abstract JpaRepository<T, Long> getRepository();

    protected abstract Collection<T> findAllUsers();

    protected abstract Long getId(T user);

    protected abstract Optional<T> findByUsernameOptional(String username);

    public AuthCredentials create(T entity) {
        User user = entity.getUser();

        String username = CredentialsGenerator.generateUsername(
                user.getFirstName(),
                user.getLastName(),
                findAllUsers()
                        .stream()
                        .map(UserComparable::getUser)
                        .toList()
        );

        String password = CredentialsGenerator.generatePassword();

        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);

        T saved = getRepository().save(entity);

        log.info("{} created successfully - ID: {}, Username: {}",
                getClass().getSimpleName().replace("Service", ""),
                getId(saved),
                username);

        return new AuthCredentials(username, password);
    }

    public T update(Long id, T updatedUser) {
        log.debug("Updating {} with ID: {}", getClass().getSimpleName(), id);

        T existing = getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        getClass().getSimpleName() + " not found with id: " + id));

        updatedUser.getUser().setUsername(existing.getUser().getUsername());
        updatedUser.getUser().setPassword(existing.getUser().getPassword());

        updatedUser = getRepository().save(updatedUser);

        log.info("{} updated successfully - ID: {}, Username: {}",
                getClass().getSimpleName(),
                id,
                updatedUser.getUser().getUsername());

        return updatedUser;
    }

    public void delete(Long id) {
        log.debug("Deleting {} with ID: {}", getClass().getSimpleName(), id);

        T user = getRepository().findById(id).orElse(null);

        if (user == null) {
            log.warn("Delete attempted for non-existent {} - ID: {}",
                    getClass().getSimpleName(), id);
            return;
        }

        getRepository().deleteById(id);

        log.info("{} deleted successfully - ID: {}, Username: {}",
                getClass().getSimpleName(),
                id,
                user.getUser().getUsername());
    }

    public T findById(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public Collection<T> findAll() {
        Collection<T> users = getRepository().findAll();

        log.debug("Retrieved {} {} from database",
                users.size(),
                getClass().getSimpleName().replace("Service", ""));

        return users;
    }

    public boolean authenticate(String username, String password) {
        return findByUsernameOptional(username)
                .map(t -> t.getUser().getPassword().equals(password))
                .orElse(false);
    }

    public boolean authenticate(Auth auth) {
        return authenticate(auth.getUsername(), auth.getPassword());
    }

    public void changePassword(String username, String newPassword) {
        T userEntity = findByUsernameOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String oldPassword = userEntity.getUser().getPassword();

        if (oldPassword.equals(newPassword)) {
            log.warn("Attempted to change to same password for user: {}", username);
            return;
        }

        userEntity.getUser().setPassword(newPassword);
        getRepository().save(userEntity);

        log.info("Password changed for user: {} at {}", username, LocalDateTime.now());
    }

    public void setActiveStatus(String username, boolean active) {
        T userEntity = findByUsernameOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean currentStatus = userEntity.getUser().getIsActive();

        if (currentStatus == active) {
            log.warn("Attempted to set same active status ({}) for user: {}", active, username);
            return;
        }

        userEntity.getUser().setIsActive(active);
        getRepository().save(userEntity);

        log.info("Active status set to {} for user: {} at {}", active, username, LocalDateTime.now());
    }

    public T findByUsername(String username) {
        return findByUsernameOptional(username).orElse(null);
    }
}