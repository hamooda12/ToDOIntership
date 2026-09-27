package org.example.todointership.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {
    @GetMapping("/protected/profile")
    public ResponseEntity<Object> profile(HttpServletRequest request) {

        Object user = request.getAttribute("user");

        return ResponseEntity.ok(user);
    }
}