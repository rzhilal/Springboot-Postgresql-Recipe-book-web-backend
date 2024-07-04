package com.arutalalab.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
  @NotBlank(message = "Kolom username tidak boleh kosong")
  @Size(min = 1, max = 100, message = "Format username belum sesuai.")
  @Pattern(regexp = "\\S+", message = "Format username belum sesuai")
  private String username;
  
  @NotBlank(message = "Kolom nama lengkap tidak boleh kosong")
  @Size(min = 1, max = 255, message = "Format nama lengkap belum sesuai. (Maksimal 255 karakter)")
  @Pattern(regexp = "^[a-zA-Z ]+$", message = "Format nama lengkap belum sesuai. (Hanya boleh huruf dan spasi)")
  private String fullname;


  @NotBlank(message = "Kolom password tidak boleh kosong")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$", message = "Password harus memiliki minimal 6 karakter kombinasi angka dan huruf")
  @Size(min = 6, max = 50, message = "Password harus kurang dari 50 karakter")
  private String password;

  @NotBlank(message = "Kolom konfirmasi password tidak boleh kosong")
  @Size(min = 6, max = 50, message = "Konfirmasi password harus kurang dari 50 karakter")
  private String retypePassword;
}
