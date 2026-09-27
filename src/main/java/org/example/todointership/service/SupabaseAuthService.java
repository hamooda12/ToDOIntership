package org.example.todointership.service;

import org.example.todointership.dto.LoginRequest;
import org.example.todointership.dto.SignupRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class SupabaseAuthService {

    private final RestClient restClient;

    public SupabaseAuthService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apikey", supabaseKey)
                .defaultHeader("Authorization", "Bearer " + supabaseKey)
                .build();
    }

    public Map signup(SignupRequest request) {

        return restClient.post()
                .uri("/auth/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "email", request.email(),
                        "password", request.password()
                ))
                .retrieve()
                .body(Map.class);
    }
    public Map login(LoginRequest request) {

        return restClient.post()
                .uri("/auth/v1/token?grant_type=password")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "email", request.email(),
                        "password", request.password()
                ))
                .retrieve()
                .body(Map.class);
    }
    public void logout(String accessToken) {

        restClient.post()
                .uri("/auth/v1/logout")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toBodilessEntity();
    }
}