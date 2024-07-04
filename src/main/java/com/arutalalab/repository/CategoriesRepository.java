package com.arutalalab.repository;

import com.arutalalab.model.Categories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> { 
  Optional<Categories> findByCategoryId(Integer categoryId);
}
