package com.arutalalab.model.response;

import com.arutalalab.model.object.SignInDataObject;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private SignInDataObject data;
    private String message;
    private int statusCode;
    private String status;
}
