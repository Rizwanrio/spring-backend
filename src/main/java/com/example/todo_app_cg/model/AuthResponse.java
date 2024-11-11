package com.example.todo_app_cg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthResponse {
    private String token;     // The JWT token
    private String username;  // Username of the authenticated user (optional)

    @Setter
    private String error;     // Error message for authentication failures

    // Constructor for successful authentication response
    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public AuthResponse() {}

    // Constructor for error response
    public AuthResponse(String error) {
        this.error = error;
    }
}
