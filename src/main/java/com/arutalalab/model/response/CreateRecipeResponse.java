package com.arutalalab.model.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeResponse {
    private String message;
    private int statusCode;
    private String status;
}
