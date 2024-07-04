package com.arutalalab.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateRecipeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private Integer categoryId;
    private String categoryName;

    private Integer levelId;
    private String levelName;

    @NotBlank(message = "Recipe name is required")
    private String recipeName;

    @NotNull(message = "Image file is required")
    private MultipartFile imageFilename;

    @NotBlank(message = "Time to cook is required")
    private String timeCook;

    @NotBlank(message = "Ingredient is required")
    private String ingredient;

    @NotBlank(message = "How to cook is required")
    private String howToCook;
}
