package com.example.gymcrm.dao;

import com.example.gymcrm.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public abstract class UserDao<T extends User> {
    protected abstract Map<Long, T> getStorage();

    protected abstract void setId(T user, Long id);

    public void save(Long id, T user) {
        setId(user, id);
        getStorage().put(id, user);
    }

    public T findById(Long id) {
        return getStorage().get(id);
    }

    public void delete(Long id) {
        getStorage().remove(id);
    }

    public Collection<T> findAll() {
        return getStorage().values();
    }

    public boolean existsByUsername(String username) {
        if (username == null) {
            return false;
        }
        return getStorage().values().stream()
                .filter(Objects::nonNull)
                .anyMatch(u -> username.equals(u.getUsername()));
    }
}
