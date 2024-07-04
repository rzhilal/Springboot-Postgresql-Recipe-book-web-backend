package com.arutalalab.repository;

import com.arutalalab.model.FavoriteFoods;
import com.arutalalab.model.id.FavoriteFoodsId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteFoodsRepository extends JpaRepository<FavoriteFoods, FavoriteFoodsId> {
  boolean existsByUserIdAndRecipeIdAndIsFavoriteTrue(Integer userId, Integer recipeId);
  Optional<FavoriteFoods> findByUserIdAndRecipeId(Integer userId, Integer recipeId);
  List<FavoriteFoods> findAllByUser_UserIdAndIsFavoriteTrue(Integer userId);
  Page<FavoriteFoods> findAllByUser_UserIdAndIsFavoriteTrue(Integer userId, Pageable pageable);
  List<FavoriteFoods> findByRecipeId(Integer recipeId);
}
