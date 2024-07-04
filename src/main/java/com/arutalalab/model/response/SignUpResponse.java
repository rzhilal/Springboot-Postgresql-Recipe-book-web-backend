package com.arutalalab.model.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
    private String message;
    private int statusCode;
    private String status;
}

