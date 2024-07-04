package com.arutalalab.model.response;

import com.arutalalab.model.object.RecipesDetailDataObject;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipesDetailResponse {
    private String Message;
    private int statusCode;
    private String status;
    private int total;
    private RecipesDetailDataObject data;
}
