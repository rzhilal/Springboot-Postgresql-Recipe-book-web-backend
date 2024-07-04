package com.arutalalab.controller;

import com.arutalalab.model.request.CreateRecipeRequest;
import com.arutalalab.model.request.ToggleFavoriteRequest;
import com.arutalalab.model.request.UpdateRecipeRequest;
import com.arutalalab.model.response.CreateRecipeResponse;
import com.arutalalab.model.response.DeleteRecipesResponse;
import com.arutalalab.model.response.RecipesDetailResponse;
import com.arutalalab.model.response.RecipesResponse;
import com.arutalalab.model.response.ToggleFavoriteResponse;
import com.arutalalab.model.response.UpdateRecipeResponse;
import com.arutalalab.service.FavoriteFoodsService;
import com.arutalalab.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/book-recipe")
public class RecipesController {

    @Autowired
    private RecipesService recipesService;

    @Autowired
    private FavoriteFoodsService favoriteFoodsService;

    @GetMapping("/book-recipes")
    public RecipesResponse getRecipes(@RequestParam(required = false) String recipeName,
            @RequestParam(required = false) Integer levelId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer time,
            @RequestParam(defaultValue = "recipeName,asc") String sortBy,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {
        return recipesService.getAllRecipes(recipeName, levelId, categoryId, time, sortBy, pageSize, pageNumber);
    }

    @GetMapping("/book-recipes/{recipesId}")
    @Secured("ROLE_USER") // Role-based authorization
    public RecipesDetailResponse getOneRecipes(@PathVariable(required = true) Integer recipesId,
            @RequestParam(required = false) Integer userId) {
        return recipesService.getOneById(recipesId, userId);
    }

    @PutMapping("/book-recipes/{recipeId}/favorites")
    public ToggleFavoriteResponse toggleFavorite(@PathVariable Integer recipeId,
            @RequestBody ToggleFavoriteRequest body) {
        return favoriteFoodsService.updateFavoriteStatus(recipeId, body);
    }

    @PostMapping("/book-recipes")
    public ResponseEntity<CreateRecipeResponse> createRecipe(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "levelId", required = false) Integer levelId,
            @RequestParam(value = "levelName", required = false) String levelName,
            @RequestParam("recipeName") String recipeName,
            @RequestParam("imageFilename") MultipartFile imageFilename,
            @RequestParam("timeCook") String timeCook,
            @RequestParam("ingredient") String ingredient,
            @RequestParam("howToCook") String howToCook) {

        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest();
        createRecipeRequest.setUserId(userId);
        createRecipeRequest.setCategoryId(categoryId);
        createRecipeRequest.setCategoryName(categoryName);
        createRecipeRequest.setLevelId(levelId);
        createRecipeRequest.setLevelName(levelName);
        createRecipeRequest.setRecipeName(recipeName);
        createRecipeRequest.setImageFilename(imageFilename);
        createRecipeRequest.setTimeCook(timeCook);
        createRecipeRequest.setIngredient(ingredient);
        createRecipeRequest.setHowToCook(howToCook);

        CreateRecipeResponse response = recipesService.createRecipe(createRecipeRequest);
        return new ResponseEntity<>(response,
                response.getStatusCode() == 201 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/book-recipes/my-recipes")
    public RecipesResponse getMyRecipes(@RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page) {
        // Set default size to 10
        Pageable pageable = PageRequest.of(page, 10);

        return recipesService.getAllMyRecipes(userId, pageable);
    }

    @DeleteMapping("/book-recipes/{recipesId}")
    public DeleteRecipesResponse deleteOneRecipes(@PathVariable(required = true) Integer recipesId) {
        return recipesService.softDeleteRecipe(recipesId);
    }

    @GetMapping("/book-recipes/my-favorite-recipes")
    public RecipesResponse getFavoriteRecipes(
            @RequestParam Integer userId,
            @RequestParam(required = false) String recipeName,
            @RequestParam(required = false) Integer levelId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer time,
            @RequestParam(defaultValue = "recipeName,asc") String sortBy,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        // Call service method to fetch favorite recipes
        return recipesService.getAllFavoriteRecipes(userId, recipeName, levelId, categoryId, time, sortBy, pageSize,
                pageNumber);
    }

    @PutMapping("/book-recipes/{recipeId}")
    public ResponseEntity<UpdateRecipeResponse> updateRecipe(
            @RequestParam("recipeId") Integer recipeId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "levelId", required = false) Integer levelId,
            @RequestParam(value = "levelName", required = false) String levelName,
            @RequestParam("recipeName") String recipeName,
            @RequestPart(value = "imageFilename", required = false) MultipartFile imageFilename,
            @RequestParam("timeCook") Integer timeCook,
            @RequestParam("ingredient") String ingredient,
            @RequestParam("howToCook") String howToCook) {

        UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest();
        updateRecipeRequest.setRecipeId(recipeId);
        updateRecipeRequest.setCategoryId(categoryId);
        updateRecipeRequest.setCategoryName(categoryName);
        updateRecipeRequest.setLevelId(levelId);
        updateRecipeRequest.setLevelName(levelName);
        updateRecipeRequest.setRecipeName(recipeName);
        updateRecipeRequest.setImageFilename(imageFilename);
        updateRecipeRequest.setTimeCook(timeCook);
        updateRecipeRequest.setIngredient(ingredient);
        updateRecipeRequest.setHowToCook(howToCook);

        UpdateRecipeResponse response = recipesService.updateRecipe(recipeId, updateRecipeRequest);
        return new ResponseEntity<>(response,
                response.getStatusCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
