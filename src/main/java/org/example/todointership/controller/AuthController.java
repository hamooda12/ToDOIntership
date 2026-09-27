package org.example.todointership.controller;

import jakarta.validation.Valid;
import org.example.todointership.dto.LoginRequest;
import org.example.todointership.dto.SignupRequest;
import org.example.todointership.service.SupabaseAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SupabaseAuthService supabaseAuthService;

    public AuthController(SupabaseAuthService supabaseAuthService) {
        this.supabaseAuthService = supabaseAuthService;
    }

    @PostMapping("/signup")
    public ResponseEntity signup(
            @Valid @RequestBody SignupRequest request
    ) {
        Map response = supabaseAuthService.signup(request);

        return ResponseEntity
                .status(201)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity login(
            @Valid @RequestBody LoginRequest request
    ) {
        Map response = supabaseAuthService.login(request);

        return ResponseEntity.ok(response);
    }

}