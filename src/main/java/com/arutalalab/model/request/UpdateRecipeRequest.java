package com.arutalalab.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateRecipeRequest {

    @NotNull(message = "Recipe ID is required")
    private Integer recipeId;

    private Integer categoryId;
    private String categoryName;

    private Integer levelId;
    private String levelName;

    @NotBlank(message = "Recipe name is required")
    private String recipeName;

    private MultipartFile imageFilename;  // This is optional for update

    @NotBlank(message = "Time to cook is required")
    private Integer timeCook;

    @NotBlank(message = "Ingredient is required")
    private String ingredient;

    private String howToCook;
}
