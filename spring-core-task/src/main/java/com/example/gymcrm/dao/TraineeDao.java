package com.example.gymcrm.dao;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDao extends UserDao<Trainee> {
    private TraineeStorage storage;

    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, Trainee> getStorage() {
        return storage.getStorage();
    }

    @Override
    protected void setId(Trainee trainee, Long id) {
        trainee.setId(id);
    }
}