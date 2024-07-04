package com.arutalalab.repository;

import com.arutalalab.model.Recipes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<Recipes, Integer>, JpaSpecificationExecutor<Recipes> {

    Page<Recipes> findAllByIsDeletedFalse(Pageable pageable);

    Page<Recipes> findAllByIsDeletedFalseAndUser_UserId(Integer userId, Pageable pageable);

    Optional<Recipes> findByRecipeId(Integer recipeId);
}
