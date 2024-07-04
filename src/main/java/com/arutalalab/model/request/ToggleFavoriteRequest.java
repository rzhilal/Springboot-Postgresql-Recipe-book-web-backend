package com.arutalalab.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ToggleFavoriteRequest {

  @NotBlank(message = "User id is required")
  private Integer userId;
}
