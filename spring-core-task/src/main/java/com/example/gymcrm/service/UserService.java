package com.example.gymcrm.service;

import com.example.gymcrm.model.User;
import com.example.gymcrm.model.UserComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public abstract class UserService<T extends UserComparable> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected abstract JpaRepository<T, Long> getRepository();

    protected abstract Collection<T> findAllUsers();

    protected abstract Long getId(T user);

    protected abstract Optional<T> findByUsernameOptional(String username);

    public T create(T entity) {

        User user = entity.getUser();

        String username = CredentialsGenerator.generateUsername(
                user.getFirstName(),
                user.getLastName(),
                getRepository().findAll()
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
                getClass().getSimpleName(),
                getId(saved),
                username);

        return saved;
    }


    public T update(Long id, T updatedUser) {
        log.debug("Updating {} with ID: {}", getClass().getSimpleName(), id);
        T existing = getRepository().findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                getClass().getSimpleName() + " not found with id: " + id
                        ));

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
                getClass().getSimpleName());

        return users;
    }

    private Collection<User> getAllUsers() {
        return findAllUsers()
                .stream()
                .map(UserComparable::getUser)
                .toList();
    }

    public boolean authenticate(String username, String password) {
        return findByUsernameOptional(username)
                .map(t -> t.getUser().getPassword().equals(password))
                .orElse(false);
    }

    public void changePassword(String username, String newPassword) {
        T userEntity = findByUsernameOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userEntity.getUser().setPassword(newPassword);
        getRepository().save(userEntity);
        log.info("{} password changed for {}", getClass().getSimpleName(), username);
    }

    public void setActiveStatus(String username, boolean active) {
        T userEntity = findByUsernameOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userEntity.getUser().setIsActive(active);
        getRepository().save(userEntity);
        log.info("{} active status set to {} for {}", getClass().getSimpleName(), active, username);
    }

}