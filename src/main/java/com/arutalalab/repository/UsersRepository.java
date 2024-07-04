package com.arutalalab.repository;

import com.arutalalab.model.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByUsername(String username);
  Boolean existsByUsername(String username);
}
