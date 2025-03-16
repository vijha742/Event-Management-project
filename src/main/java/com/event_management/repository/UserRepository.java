package com.event_management.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.event_management.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
   @Query("SELECT u FROM User u")
    List<User> findAllWithAdminAndRegistrations();

   @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserWithAdminAndRegistrations();
}
