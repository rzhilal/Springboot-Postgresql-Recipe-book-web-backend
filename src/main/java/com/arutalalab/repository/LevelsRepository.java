package com.arutalalab.repository;

import com.arutalalab.model.Levels;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelsRepository extends JpaRepository<Levels, Long> {
  Optional<Levels> findByLevelId(Integer levelsId);
}
