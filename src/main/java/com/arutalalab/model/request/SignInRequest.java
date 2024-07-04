package com.arutalalab.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
  @NotBlank(message = "Username is required")
  @Email(message = "Username is not valid")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;
}
