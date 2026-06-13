package com.zebiso.Backend.repository;

import com.zebiso.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByNameIgnoreCase(String name);
}
