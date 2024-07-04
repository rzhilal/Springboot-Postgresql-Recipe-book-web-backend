package com.arutalalab.service;

import com.arutalalab.model.FavoriteFoods;
import com.arutalalab.model.request.ToggleFavoriteRequest;
import com.arutalalab.model.response.ToggleFavoriteResponse;
import com.arutalalab.repository.FavoriteFoodsRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteFoodsService {

    @Autowired
    private FavoriteFoodsRepository favoriteFoodsRepository;

    @Transactional
    public ToggleFavoriteResponse updateFavoriteStatus(Integer recipeId, ToggleFavoriteRequest body) {
        ToggleFavoriteResponse response = new ToggleFavoriteResponse();
        Integer userId = body.getUserId();
        try {
            // Check if the favorite entry exists
            Optional<FavoriteFoods> favoriteFoodOptional = favoriteFoodsRepository.findByUserIdAndRecipeId(userId, recipeId);
            if (favoriteFoodOptional.isPresent()) {
                FavoriteFoods favoriteFood = favoriteFoodOptional.get();
                favoriteFood.setIsFavorite(!favoriteFood.getIsFavorite());
                favoriteFoodsRepository.save(favoriteFood);

                // Set response data for updating favorite status
                response.setTotal(1);
                response.setMessage("Berhasil mengubah status favorit");
                response.setStatusCode(200);
                response.setStatus("OK");
            } else {
                // Create new favorite entry
                FavoriteFoods newFavoriteFood = FavoriteFoods.builder()
                        .userId(userId)
                        .recipeId(recipeId)
                        .isFavorite(true)
                        .build();
                favoriteFoodsRepository.save(newFavoriteFood);

                // Set response data for creating new favorite entry
                response.setTotal(1);
                response.setMessage("Berhasil menambahkan resep ke favorit");
                response.setStatusCode(200);
                response.setStatus("OK");
            }
        } catch (Exception e) {
            // If there's an error, set error response
            response.setTotal(0);
            response.setData(null);
            response.setMessage("Terjadi kesalahan server. Silakan coba kembali.");
            response.setStatusCode(500);
            response.setStatus("Kesalahan server");
        }

        return response;
    }

}
