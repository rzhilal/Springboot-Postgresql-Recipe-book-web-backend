package com.arutalalab.model.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeResponse {
    private String message;
    private int statusCode;
    private String status;
}
