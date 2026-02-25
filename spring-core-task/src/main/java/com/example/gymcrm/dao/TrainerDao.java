package com.example.gymcrm.dao;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Repository
public class TrainerDao extends UserDao<Trainer> {
    private TrainerStorage storage;

    @Autowired
    public void setStorage(TrainerStorage storage) {
        this.storage = storage;
    }

    @Override
    protected Map<Long, Trainer> getStorage() {
        return storage.getStorage();
    }

    @Override
    protected void setId(Trainer trainer, Long id) {
        trainer.setId(id);
    }
}