package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUser_Username(String username);

    @Query("""
        select tr from Trainer tr
        where tr not in (
            select t from Trainee ta join ta.trainers t
            where ta.user.username = :username
        )
    """)
    List<Trainer> findNotAssignedToTrainee(@Param("username") String username);
}