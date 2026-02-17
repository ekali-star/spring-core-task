package com.example.gymcrm.dao;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TrainerDao {
    private TrainerStorage storage;

    @Autowired
    public void setStorage(TrainerStorage storage) {
        this.storage = storage;
    }
    public void save(Long id, Trainer trainer) {
        storage.getStorage().put(id, trainer);
    }

    public Trainer findById(Long id) {
        return storage.getStorage().get(id);
    }

    public void delete(Long id) {
        storage.getStorage().remove(id);
    }

    public Collection<Trainer> findAll() {
        return storage.getStorage().values();
    }
}
