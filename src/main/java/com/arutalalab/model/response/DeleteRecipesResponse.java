package com.arutalalab.model.response;

import java.util.List;

import com.arutalalab.model.object.RecipesDataObject;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRecipesResponse {
    private String Message;
    private int statusCode;
    private String status;
    private int total;
    private List<RecipesDataObject> data;
}
