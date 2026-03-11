package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    @Query("SELECT t FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username")
    Optional<Trainee> findByUserUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {"trainers", "trainers.user"})
    @Query("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username")
    Optional<Trainee> findByUserUsernameWithTrainers(@Param("username") String username);
}