package com.arutalalab.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arutalalab.model.response.CategoriesListResponse;
import com.arutalalab.model.Categories;
import com.arutalalab.model.object.CategoryRecipeObject;
import com.arutalalab.repository.CategoriesRepository;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public CategoriesListResponse getCategoriesOptionList() {
        // Fetch all categories from the repository
        List<Categories> categories = categoriesRepository.findAll();

        // Map Categories entities to CategoryRecipeObject
        List<CategoryRecipeObject> categoryRecipeObjects = categories.stream()
                .map(category -> CategoryRecipeObject.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .build())
                .collect(Collectors.toList());

        // Create the response object
        CategoriesListResponse response = CategoriesListResponse.builder()
                .Message("Categories fetched successfully")
                .statusCode(200)
                .status("success")
                .data(categoryRecipeObjects)
                .build();

        return response;
    }
}
