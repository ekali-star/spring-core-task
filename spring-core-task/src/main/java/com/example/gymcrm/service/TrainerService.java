package com.example.gymcrm.service;

import com.example.gymcrm.dao.TrainerDao;
import com.example.gymcrm.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrainerService extends UserService<Trainer> {
    private TrainerDao trainerDao;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Override
    protected TrainerDao getDao() {
        return trainerDao;
    }

    @Override
    protected Trainer saveWithId(Trainer user) {
        long id = idGenerator.getAndIncrement();
        user.setId(id);
        trainerDao.save(id, user);
        return user;
    }

    @Override
    protected Long getId(Trainer user) {
        return user.getId();
    }
}