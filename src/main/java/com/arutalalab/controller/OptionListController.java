package com.arutalalab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arutalalab.model.response.CategoriesListResponse;
import com.arutalalab.model.response.LevelsListResponse;
import com.arutalalab.service.CategoriesService;
import com.arutalalab.service.LevelsService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/book-recipe-masters")
public class OptionListController {
    
    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private LevelsService levelsService;

    @GetMapping("/category-option-lists")
    public CategoriesListResponse getCategoryOptionLists() {
        return categoriesService.getCategoriesOptionList();
    }

    @GetMapping("/level-option-lists")
    public LevelsListResponse getLevelOptionLists() {
        return levelsService.getLevelsList();
    }
    
}
