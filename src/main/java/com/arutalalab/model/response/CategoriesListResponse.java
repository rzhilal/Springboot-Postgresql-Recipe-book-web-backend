package com.arutalalab.model.response;

import java.util.List;

import com.arutalalab.model.object.CategoryRecipeObject;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesListResponse {
    private String Message;
    private int statusCode;
    private String status;
    private List<CategoryRecipeObject> data;
}
