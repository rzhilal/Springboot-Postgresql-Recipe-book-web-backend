package com.arutalalab.controller;

import com.arutalalab.model.request.SignInRequest;
import com.arutalalab.model.request.SignUpRequest;
import com.arutalalab.model.response.SignInResponse;
import com.arutalalab.model.response.SignUpResponse;
import com.arutalalab.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        var response = usersService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/users/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest body) {
        SignInResponse response = usersService.signIn(body);
        if (response.getStatusCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @PostMapping("/users/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest body) {
        SignUpResponse response = usersService.signUp(body);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
}
