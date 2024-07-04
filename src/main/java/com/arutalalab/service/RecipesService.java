package com.arutalalab.service;

import com.arutalalab.model.Categories;
import com.arutalalab.model.FavoriteFoods;
import com.arutalalab.model.Levels;
import com.arutalalab.model.Recipes;
import com.arutalalab.model.Users;
import com.arutalalab.model.object.CategoryRecipeObject;
import com.arutalalab.model.object.LevelRecipeObject;
import com.arutalalab.model.object.RecipesDataObject;
import com.arutalalab.model.object.RecipesDetailDataObject;
import com.arutalalab.model.request.CreateRecipeRequest;
import com.arutalalab.model.request.UpdateRecipeRequest;
import com.arutalalab.model.response.CreateRecipeResponse;
import com.arutalalab.model.response.DeleteRecipesResponse;
import com.arutalalab.model.response.RecipesDetailResponse;
import com.arutalalab.model.response.RecipesResponse;
import com.arutalalab.model.response.UpdateRecipeResponse;
import com.arutalalab.repository.CategoriesRepository;
import com.arutalalab.repository.FavoriteFoodsRepository;
import com.arutalalab.repository.LevelsRepository;
import com.arutalalab.repository.RecipesRepository;
import com.arutalalab.repository.UsersRepository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;
import lib.minio.MinioService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecipesService {

        @Autowired
        private RecipesRepository recipesRepository;

        @Autowired
        private FavoriteFoodsRepository favoriteFoodsRepository;

        @Autowired
        private UsersRepository usersRepository;

        @Autowired
        private CategoriesRepository categoriesRepository;

        @Autowired
        private LevelsRepository levelsRepository;

        @Autowired
        private MinioService minioService;

        public RecipesResponse getAllRecipes(String recipeName, Integer levelId, Integer categoryId, Integer time,
                        String sortBy, Integer pageSize, Integer pageNumber) {
                // Process sortBy parameter to extract field and direction
                String[] sortParams = sortBy.split(",");
                String sortField = sortParams[0];
                Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);

                // Create Pageable instance with sorting by the provided field and direction
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));

                // Call the recipesRepository method with filters
                Page<Recipes> recipesPage = recipesRepository.findAll((root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        // Default filter is_deleted = FALSE
                        predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

                        // Apply additional filters based on request parameters
                        if (recipeName != null && !recipeName.isEmpty()) {
                                predicates.add(criteriaBuilder.like(root.get("recipeName"), "%" + recipeName + "%"));
                        }
                        if (levelId != null) {
                                predicates.add(criteriaBuilder.equal(root.get("level").get("levelId"), levelId));
                        }
                        if (categoryId != null) {
                                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"),
                                                categoryId));
                        }
                        if (time != null) {
                                predicates.add(criteriaBuilder.equal(root.get("timeCook"), time));
                        }

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Recipes> recipesList = recipesPage.getContent();

                List<RecipesDataObject> dataResponse = recipesList.stream()
                                .map(recipe -> {
                                        CategoryRecipeObject categoryData = CategoryRecipeObject.builder()
                                                        .categoryId(recipe.getCategory().getCategoryId())
                                                        .categoryName(recipe.getCategory().getCategoryName())
                                                        .build();

                                        LevelRecipeObject levelData = LevelRecipeObject.builder()
                                                        .levelId(recipe.getLevel().getLevelId())
                                                        .levelName(recipe.getLevel().getLevelName())
                                                        .build();

                                        return RecipesDataObject.builder()
                                                        .recipeId(recipe.getRecipeId())
                                                        .categories(categoryData)
                                                        .levels(levelData)
                                                        .recipeName(recipe.getRecipeName())
                                                        .imageUrl(minioService.getFileLink(recipe.getImageFilename()))
                                                        .time(recipe.getTimeCook())
                                                        .isFavorite(favoriteFoodsRepository
                                                                        .existsByUserIdAndRecipeIdAndIsFavoriteTrue(
                                                                                        recipe.getUser().getUserId(),
                                                                                        recipe.getRecipeId()))
                                                        .build();
                                })
                                .collect(Collectors.toList());

                return RecipesResponse.builder()
                                .Message("Success")
                                .statusCode(200)
                                .status("OK")
                                .total((int) recipesPage.getTotalElements())
                                .data(dataResponse)
                                .build();
        }

        public RecipesDetailResponse getOneById(int recipeId, int userId) {
                Optional<Recipes> recipe = recipesRepository.findByRecipeId(recipeId);

                if (recipe.isPresent()) {
                        CategoryRecipeObject categoryRecipeObject = CategoryRecipeObject.builder()
                                        .categoryId(recipe.get().getCategory().getCategoryId())
                                        .categoryName(recipe.get().getCategory().getCategoryName())
                                        .build();

                        LevelRecipeObject levelRecipeObject = LevelRecipeObject.builder()
                                        .levelId(recipe.get().getLevel().getLevelId())
                                        .levelName(recipe.get().getLevel().getLevelName())
                                        .build();

                        RecipesDetailDataObject recipeObject = RecipesDetailDataObject.builder()
                                        .recipeId(recipe.get().getRecipeId())
                                        .categories(categoryRecipeObject)
                                        .levels(levelRecipeObject)
                                        .recipeName(recipe.get().getRecipeName())
                                        .imageUrl(minioService.getFileLink(recipe.get().getImageFilename()))
                                        .isFavorite(favoriteFoodsRepository.existsByUserIdAndRecipeIdAndIsFavoriteTrue(
                                                        userId,
                                                        recipe.get().getRecipeId()))
                                        .ingredient(recipe.get().getIngredient())
                                        .howToCook(recipe.get().getHowToCook())
                                        .time(recipe.get().getTime())
                                        .build();

                        return RecipesDetailResponse.builder()
                                        .Message("Berhasil Memuat Resep " + recipeObject.getRecipeName())
                                        .statusCode(200)
                                        .status("OK")
                                        .total(1)
                                        .data(recipeObject)
                                        .build();
                } else {
                        return RecipesDetailResponse.builder()
                                        .Message("Terjadi kesalahan server. Silakan coba kembali.")
                                        .statusCode(500)
                                        .status("Internal Server Error")
                                        .total(0)
                                        .data(null)
                                        .build();
                }
        }

        @Transactional
        public CreateRecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
                try {
                        // Retrieve user, category, and level from the respective repositories
                        Users user = usersRepository.findById(createRecipeRequest.getUserId())
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        Categories category = categoriesRepository
                                        .findByCategoryId(createRecipeRequest.getCategoryId())
                                        .orElseThrow(() -> new RuntimeException("Category not found"));

                        Levels level = levelsRepository.findByLevelId(createRecipeRequest.getLevelId())
                                        .orElseThrow(() -> new RuntimeException("Level not found"));

                        
                        // Get the original filename and check the file extension
                        String originalFilename = createRecipeRequest.getImageFilename().getOriginalFilename();
                        if (originalFilename == null ||
                                        !(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg")
                                                        || originalFilename.endsWith(".png"))) {
                                return CreateRecipeResponse.builder()
                                                .message("Invalid file type. Only .jpg, .jpeg, .png are allowed.")
                                                .statusCode(400)
                                                .status("Failed")
                                                .build();
                        }
                        
                        // Create the new filename
                        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                                             .format(ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
                        
                        String newFilename = String.format("%s_%s_%s_%s.%s",
                                        createRecipeRequest.getRecipeName(),
                                        createRecipeRequest.getCategoryId(),
                                        createRecipeRequest.getLevelId(),
                                        timestamp,
                                        originalFilename.substring(originalFilename.lastIndexOf(".") + 1));

                        // Upload the image file to Minio
                        String imageFilename = minioService.uploadFile(createRecipeRequest.getImageFilename(),
                                        newFilename);
                        // Create and save the recipe
                        Recipes recipe = Recipes.builder()
                                        .user(user)
                                        .category(category)
                                        .level(level)
                                        .recipeName(createRecipeRequest.getRecipeName())
                                        .imageFilename(imageFilename)
                                        .imageUrl(null)
                                        .timeCook(Integer.parseInt(createRecipeRequest.getTimeCook()))
                                        .time(Integer.parseInt(createRecipeRequest.getTimeCook()))
                                        .ingredient(createRecipeRequest.getIngredient())
                                        .howToCook(createRecipeRequest.getHowToCook())
                                        .isDeleted(false)
                                        .createdBy(user.getUsername())
                                        .build();

                        recipesRepository.save(recipe);
                        System.out.println("Timestamp: " + recipe.getRecipeId());
                        return CreateRecipeResponse.builder()
                                        .message("Recipe created successfully.")
                                        .statusCode(201)
                                        .status("Success")
                                        .build();

                } catch (Exception e) {
                        return CreateRecipeResponse.builder()
                                        .message("An error occurred: " + e.getMessage())
                                        .statusCode(500)
                                        .status("Failed")
                                        .build();
                }
        }

        @Transactional
        public UpdateRecipeResponse updateRecipe(Integer recipeId, UpdateRecipeRequest updateRecipeRequest) {
                try {
                        // Retrieve the recipe, user, category, and level from the respective
                        // repositories
                        Recipes recipe = recipesRepository.findByRecipeId(recipeId)
                                        .orElseThrow(() -> new RuntimeException("Recipe not found"));

                        Categories category = categoriesRepository
                                        .findByCategoryId(updateRecipeRequest.getCategoryId())
                                        .orElseThrow(() -> new RuntimeException("Category not found"));

                        Levels level = levelsRepository.findByLevelId(updateRecipeRequest.getLevelId())
                                        .orElseThrow(() -> new RuntimeException("Level not found"));

                        // Get the original filename and check the file extension
                        String originalFilename = updateRecipeRequest.getImageFilename().getOriginalFilename();
                        if (originalFilename != null &&
                                        !(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg")
                                                        || originalFilename.endsWith(".png"))) {
                                return UpdateRecipeResponse.builder()
                                                .message("Invalid file type. Only .jpg, .jpeg, .png are allowed.")
                                                .statusCode(400)
                                                .status("Failed")
                                                .build();
                        }

                        // Create the new filename if an image is provided
                        String imageFilename = recipe.getImageFilename();
                        if (originalFilename != null) {
                                String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                                             .format(ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
                                String newFilename = String.format("%s_%s_%s_%s.%s",
                                                updateRecipeRequest.getRecipeName(),
                                                updateRecipeRequest.getCategoryId(),
                                                updateRecipeRequest.getLevelId(),
                                                timestamp,
                                                originalFilename.substring(originalFilename.lastIndexOf(".") + 1));

                                // Upload the new image file to Minio and get the filename
                                imageFilename = minioService.uploadFile(updateRecipeRequest.getImageFilename(),
                                                newFilename);
                        }

                        // Update the recipe details
                        recipe.setCategory(category);
                        recipe.setLevel(level);
                        recipe.setRecipeName(updateRecipeRequest.getRecipeName());
                        recipe.setImageFilename(imageFilename);
                        recipe.setImageUrl(null); // tidak terpakai
                        recipe.setTime(updateRecipeRequest.getTimeCook());
                        recipe.setTimeCook(updateRecipeRequest.getTimeCook());
                        recipe.setIngredient(updateRecipeRequest.getIngredient());
                        recipe.setHowToCook(updateRecipeRequest.getHowToCook());
                        recipe.setModifiedBy(recipe.getUser().getUsername());

                        recipesRepository.save(recipe);

                        return UpdateRecipeResponse.builder()
                                        .message(String.format("Resep %s berhasil diubah!",
                                                        updateRecipeRequest.getRecipeName()))
                                        .statusCode(200)
                                        .status("Success")
                                        .build();

                } catch (Exception e) {
                        return UpdateRecipeResponse.builder()
                                        .message("An error occurred: " + e.getMessage())
                                        .statusCode(500)
                                        .status("Failed")
                                        .build();
                }
        }

        public RecipesResponse getAllFavoriteRecipes(Integer userId, String recipeName, Integer levelId,
                        Integer categoryId, Integer time, String sortBy, Integer pageSize, Integer pageNumber) {
                // Process sortBy parameter to extract field and direction
                String[] sortParams = sortBy.split(",");
                String sortField = sortParams[0];
                Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);

                // Create Pageable instance with sorting by the provided field and direction
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));

                // Step 1: Get IDs of all favorited recipes by the user
                List<FavoriteFoods> favoriteFoodsList = favoriteFoodsRepository
                                .findAllByUser_UserIdAndIsFavoriteTrue(userId);

                List<Integer> favoritedRecipeIds = favoriteFoodsList.stream()
                                .map(FavoriteFoods::getRecipeId)
                                .collect(Collectors.toList());

                // Step 3: Query recipes that are favorited and match other filters
                Page<Recipes> recipesPage = recipesRepository.findAll((root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        // Default filter is_deleted = FALSE
                        predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

                        // Apply additional filters based on request parameters
                        if (recipeName != null && !recipeName.isEmpty()) {
                                predicates.add(criteriaBuilder.like(root.get("recipeName"), "%" + recipeName + "%"));
                        }
                        if (levelId != null) {
                                predicates.add(criteriaBuilder.equal(root.get("level").get("levelId"), levelId));
                        }
                        if (categoryId != null) {
                                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"),
                                                categoryId));
                        }
                        if (time != null) {
                                predicates.add(criteriaBuilder.equal(root.get("timeCook"), time));
                        }
                        // Filter by favorited recipe IDs
                        predicates.add(root.get("recipeId").in(favoritedRecipeIds));

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Recipes> recipesList = recipesPage.getContent();

                // Step 4: Build response with recipe details
                List<RecipesDataObject> dataResponse = recipesList.stream()
                                .map(recipe -> {
                                        CategoryRecipeObject categoryData = CategoryRecipeObject.builder()
                                                        .categoryId(recipe.getCategory().getCategoryId())
                                                        .categoryName(recipe.getCategory().getCategoryName())
                                                        .build();

                                        LevelRecipeObject levelData = LevelRecipeObject.builder()
                                                        .levelId(recipe.getLevel().getLevelId())
                                                        .levelName(recipe.getLevel().getLevelName())
                                                        .build();

                                        return RecipesDataObject.builder()
                                                        .recipeId(recipe.getRecipeId())
                                                        .categories(categoryData)
                                                        .levels(levelData)
                                                        .recipeName(recipe.getRecipeName())
                                                        .imageUrl(minioService.getFileLink(recipe.getImageFilename()))
                                                        .time(recipe.getTimeCook())
                                                        .isFavorite(true)
                                                        .build();
                                })
                                .collect(Collectors.toList());

                return RecipesResponse.builder()
                                .Message("Success")
                                .statusCode(200)
                                .status("OK")
                                .total((int) recipesPage.getTotalElements())
                                .data(dataResponse)
                                .build();
        }

        public DeleteRecipesResponse softDeleteRecipe(Integer recipeId) {
                Recipes recipe = recipesRepository.findByRecipeId(recipeId)
                                .orElseThrow(() -> new RuntimeException("Recipe not found"));

                recipe.setIsDeleted(true);

                recipesRepository.save(recipe);

                List<FavoriteFoods> favoriteFoods = favoriteFoodsRepository.findByRecipeId(recipeId);

                // Update all related favoriteFoods to set isFavorite to false
                favoriteFoods.forEach(favoriteFood -> favoriteFood.setIsFavorite(false));
                favoriteFoodsRepository.saveAll(favoriteFoods);

                return DeleteRecipesResponse.builder()
                                .Message(String.format("Resep %s berhasil dihapus!", recipe.getRecipeName()))
                                .statusCode(200)
                                .status("Success")
                                .build();
        }

        public RecipesResponse getAllMyRecipes(Integer userId, Pageable pageable) {
                Page<Recipes> recipesPage = recipesRepository.findAllByIsDeletedFalseAndUser_UserId(userId, pageable);

                List<RecipesDataObject> dataResponse = recipesPage.getContent().stream()
                                .map(recipe -> {
                                        // Mapping logic remains the same as before
                                        CategoryRecipeObject categoryData = CategoryRecipeObject.builder()
                                                        .categoryId(recipe.getCategory().getCategoryId())
                                                        .categoryName(recipe.getCategory().getCategoryName())
                                                        .build();

                                        LevelRecipeObject levelData = LevelRecipeObject.builder()
                                                        .levelId(recipe.getLevel().getLevelId())
                                                        .levelName(recipe.getLevel().getLevelName())
                                                        .build();
                                        return RecipesDataObject.builder()
                                                        .recipeId(recipe.getRecipeId())
                                                        .categories(categoryData)
                                                        .levels(levelData)
                                                        .recipeName(recipe.getRecipeName())
                                                        .imageUrl(minioService.getFileLink(recipe.getImageFilename()))
                                                        .time(recipe.getTimeCook())
                                                        .isFavorite(favoriteFoodsRepository
                                                                        .existsByUserIdAndRecipeIdAndIsFavoriteTrue(
                                                                                        userId, recipe.getRecipeId()))
                                                        .build();
                                })
                                .sorted(Comparator.comparing(RecipesDataObject::getRecipeName)) // Sorting ascending by
                                                                                                // recipeName
                                .collect(Collectors.toList());

                return RecipesResponse.builder()
                                .Message("Success")
                                .statusCode(200)
                                .status("OK")
                                .total((int) recipesPage.getTotalElements())
                                .data(dataResponse)
                                .build();
        }
}
