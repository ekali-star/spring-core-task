package com.example.gymcrm.repository;

import com.example.gymcrm.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    @Override
    default <S extends TrainingType> S save(S entity) {
        throw new UnsupportedOperationException("Training types cannot be modified");
    }

    @Override
    default void delete(TrainingType entity) {
        throw new UnsupportedOperationException("Training types cannot be modified");
    }
}