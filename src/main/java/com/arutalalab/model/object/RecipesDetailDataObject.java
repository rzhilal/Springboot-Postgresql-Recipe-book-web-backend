package com.arutalalab.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipesDetailDataObject {
    private int recipeId;
    private CategoryRecipeObject categories;
    private LevelRecipeObject levels;
    private String recipeName;
    private String imageUrl;
    private int time;
    
    @JsonProperty("isFavorite")
    private boolean isFavorite;

    private String ingredient;
    private String howToCook;
}
