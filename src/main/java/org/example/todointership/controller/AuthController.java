package org.example.todointership.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
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
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        String accessToken = (String) request.getAttribute("accessToken");

        supabaseAuthService.logout(accessToken);

        return ResponseEntity.noContent().build();
    }

}