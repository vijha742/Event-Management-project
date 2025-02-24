package com.event_management.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.event_management.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT DISTINCT u FROM User u " +
       "LEFT JOIN FETCH u.eventRegistrations er " +
       "LEFT JOIN FETCH er.event")
    List<User> findAllWithAdminAndRegistrations();
}
