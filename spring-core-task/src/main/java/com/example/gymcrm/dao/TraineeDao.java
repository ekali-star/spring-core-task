package com.example.gymcrm.dao;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TraineeDao {
    private TraineeStorage storage;

    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
    }
    public void save(Long id, Trainee trainee) {
        storage.getStorage().put(id, trainee);
    }

    public Trainee findById(Long id) {
        return storage.getStorage().get(id);
    }

    public void delete(Long id) {
        storage.getStorage().remove(id);
    }

    public Collection<Trainee> findAll() {
        return storage.getStorage().values();
    }

}
