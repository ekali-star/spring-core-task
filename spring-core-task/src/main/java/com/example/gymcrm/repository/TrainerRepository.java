package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("SELECT t FROM Trainer t JOIN FETCH t.user u WHERE u.username = :username")
    Optional<Trainer> findByUserUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t JOIN FETCH t.user u WHERE u.username IN :usernames")
    List<Trainer> findByUserUsernameIn(@Param("usernames") Set<String> usernames);

    @Query("SELECT t FROM Trainer t WHERE t NOT IN " +
            "(SELECT tr FROM Trainee tn JOIN tn.trainers tr WHERE tn.user.username = :traineeUsername)")
    List<Trainer> findNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);
}