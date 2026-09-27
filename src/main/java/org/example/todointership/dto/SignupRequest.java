package org.example.todointership.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}