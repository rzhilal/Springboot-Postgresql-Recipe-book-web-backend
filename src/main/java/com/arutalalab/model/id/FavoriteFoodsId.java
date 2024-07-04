package com.arutalalab.model.id;

import java.io.Serializable;

import lombok.Data;

@Data
public class FavoriteFoodsId implements Serializable {
    private Integer userId; 
    private Integer recipeId;
}

