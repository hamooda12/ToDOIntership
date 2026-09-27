package org.example.todointership.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

    @GetMapping("/protected/profile")
    public ResponseEntity<Object> profile(HttpServletRequest request) {
        Object user = request.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/protected/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpServletRequest request) {
        Object user = request.getAttribute("user");
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to the protected dashboard",
                "user", user
        ));
    }
}
