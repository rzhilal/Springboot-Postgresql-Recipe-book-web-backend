package com.arutalalab.model.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInDataObject {
    private Integer id;
    private String token;
    private String type;
    private String username;
    private String role;
}