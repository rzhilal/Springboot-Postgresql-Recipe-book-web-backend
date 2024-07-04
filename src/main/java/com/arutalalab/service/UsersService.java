package com.arutalalab.service;

import com.arutalalab.model.response.SignUpResponse;
import com.arutalalab.model.Users;
import com.arutalalab.model.object.SignInDataObject;
import com.arutalalab.repository.UsersRepository;
import com.arutalalab.security.JwtService;
import com.arutalalab.model.request.SignInRequest;
import com.arutalalab.model.request.SignUpRequest;
import com.arutalalab.model.response.SignInResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersService {
  private final UsersRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public List<Users> getAllUsers() {
    return repository.findAll();
  }

  public SignInResponse signIn(SignInRequest body) {
    Optional<Users> userOptional = repository.findByUsername(body.getUsername());

    if (userOptional.isEmpty() || !passwordEncoder.matches(body.getPassword(), userOptional.get().getPassword())) {
      return SignInResponse.builder()
          .message("Username atau password salah")
          .statusCode(401)
          .status("Unauthorized")
          .build();
    }

    Users user = userOptional.get();
    System.out.println("-------------------------------------------------------------------");
    String jwtToken = jwtService.generateToken(user.getUsername());
    SignInDataObject signInData = SignInDataObject.builder()
        .id(user.getUserId())
        .token(jwtToken)
        .type("Bearer")
        .username(user.getUsername())
        .role(user.getRole())
        .build();

    return SignInResponse.builder()
        .data(signInData)
        .message("Sign in successful")
        .statusCode(200)
        .status("OK")
        .build();
  }

  public SignUpResponse signUp(SignUpRequest body) {
    // Check if username is already taken
    if (repository.existsByUsername(body.getUsername())) {
      return SignUpResponse.builder()
          .message("Username telah digunakan oleh user yang telah mendaftar sebelumnya")
          .statusCode(409)
          .status("Conflict")
          .build();
    }

    // Check if password matches confirm password
    if (!body.getPassword().equals(body.getRetypePassword())) {
      return SignUpResponse.builder()
          .message("Konfirmasi kata sandi tidak sama dengan kata sandi.")
          .statusCode(400)
          .status("Bad Request")
          .build();
    }

    // Hash the password
    String hashedPassword = passwordEncoder.encode(body.getPassword());

    // Create user entity
    Users user = Users.builder()
        .fullname(body.getFullname())
        .username(body.getUsername())
        .password(hashedPassword)
        .role("User")
        .isDeleted(false)
        .build();

    try {
      user = repository.save(user);
      // Build success response
      return SignUpResponse.builder()
          .message("Berhasil menambahkan " + body.getUsername())
          .statusCode(200)
          .status("OK")
          .build();
    } catch (Exception e) {
      return SignUpResponse.builder()
          .message("Terjadi kesalahan server. Silakan coba kembali.")
          .statusCode(500)
          .status("Internal Server Error")
          .build();
    }
  }

}