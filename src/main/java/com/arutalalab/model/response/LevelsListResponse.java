package com.arutalalab.model.response;

import java.util.List;

import com.arutalalab.model.object.LevelRecipeObject;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevelsListResponse {
    private String Message;
    private int statusCode;
    private String status;
    private List<LevelRecipeObject> data;
}
