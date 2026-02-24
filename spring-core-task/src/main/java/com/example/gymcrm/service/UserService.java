package com.example.gymcrm.service;

import com.example.gymcrm.dao.UserDao;
import com.example.gymcrm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public abstract class UserService<T extends User> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected abstract UserDao<T> getDao();

    public T create(T user) {
        String username = CredentialsGenerator.generateUsername(
                user.getFirstName(),
                user.getLastName(),
                getDao().findAll()
        );
        String password = CredentialsGenerator.generatePassword();
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);
        T saved = saveWithId(user);
        log.info("{} created successfully - ID: {}, Username: {}",
                getClass().getSimpleName(),
                getId(saved),
                username);
        return saved;
    }

    protected abstract T saveWithId(T user);

    protected abstract Long getId(T user);

    public T update(Long id, T user) {
        log.debug("Updating {} with ID: {}", getClass().getSimpleName(), id);
        T existing = getDao().findById(id);
        if (existing == null) {
            log.error("Update failed - {} not found with ID: {}", getClass().getSimpleName(), id);
            throw new IllegalArgumentException(getClass().getSimpleName() + " not found with id: " + id);
        }
        user.setUsername(existing.getUsername());
        user.setPassword(existing.getPassword());
        getDao().save(id, user);
        log.info("{} updated successfully - ID: {}, Username: {}", getClass().getSimpleName(), id, user.getUsername());
        return user;
    }

    public void delete(Long id) {
        log.debug("Deleting {} with ID: {}", getClass().getSimpleName(), id);
        T user = getDao().findById(id);
        if (user == null) {
            log.warn("Delete attempted for non-existent {} - ID: {}", getClass().getSimpleName(), id);
            return;
        }
        getDao().delete(id);
        log.info("{} deleted successfully - ID: {}, Username: {}", getClass().getSimpleName(), id, user.getUsername());
    }

    public T findById(Long id) {
        T user = getDao().findById(id);
        if (user == null) {
            log.debug("{} not found with ID: {}", getClass().getSimpleName(), id);
        } else {
            log.debug("{} found - ID: {}, Username: {}", getClass().getSimpleName(), id, user.getUsername());
        }
        return user;
    }

    public Collection<T> findAll() {
        Collection<T> users = getDao().findAll();
        log.debug("Retrieved {} {} from storage", users.size(), getClass().getSimpleName());
        return users;
    }
}