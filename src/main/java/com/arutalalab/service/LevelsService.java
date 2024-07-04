package com.arutalalab.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arutalalab.model.Levels;
import com.arutalalab.model.object.LevelRecipeObject;
import com.arutalalab.model.response.LevelsListResponse;
import com.arutalalab.repository.LevelsRepository;

@Service
public class LevelsService {

    @Autowired
    private LevelsRepository levelsRepository;

    public LevelsListResponse getLevelsList() {
        List<Levels> levels = levelsRepository.findAll();
        List<LevelRecipeObject> levelRecipeObjects = levels.stream()
                .map(level -> LevelRecipeObject.builder()
                        .levelId(level.getLevelId())
                        .levelName(level.getLevelName())
                        .build())
                .collect(Collectors.toList());
        
        // Create the response object
        LevelsListResponse response = LevelsListResponse.builder()
                .Message("Level list fetched successfully")
                .statusCode(200)
                .status("success")
                .data(levelRecipeObjects)
                .build();

        return response;
    }
}
