package com.example.todo_app_cg.controller;

import com.example.todo_app_cg.model.AuthRequest;
import com.example.todo_app_cg.model.AuthResponse;
import com.example.todo_app_cg.model.User;
import com.example.todo_app_cg.service.UserService;
import com.example.todo_app_cg.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil; // Assuming this class generates JWTs

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody  AuthRequest authRequest) {
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(authRequest.getPassword());

        userService.register(user);
        String message = "User registered successfully";
        AuthResponse authResponse = new AuthResponse(user.getUsername(),message);
        return ResponseEntity.ok(authResponse);
    }

 //  @GetMapping("/csrf")
  //  public ResponseEntity<CsrfToken> csrf(CsrfToken token) {
   //     return ResponseEntity.ok(token);
  //  }

    // Log in a user
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtUtil.generateToken(authRequest.getUsername());

        Cookie authCookie = new Cookie("auth_token", token);
        System.out.println("this is cookie while setting: "+authCookie);
        authCookie.setHttpOnly(false);
       // authCookie.setSecure(true); // Only for HTTPS
        authCookie.setPath("/");
        authCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(authCookie);
        System.out.println("this is final cookie: "+authCookie);
        // Create AuthResponse object
        AuthResponse authResponse = new AuthResponse(token, authRequest.getUsername());

        return ResponseEntity.ok(authResponse);
        }catch (AuthenticationException ex) {
            // If authentication fails, create an AuthResponse with the error message
            AuthResponse errorResponse = new AuthResponse(ex.getMessage()); // Using the error-only constructor
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie authCookie = new Cookie("auth_token", null);
        authCookie.setPath("/");
        authCookie.setHttpOnly(false);
        authCookie.setMaxAge(0); // Set the age to 0 to delete it
        response.addCookie(authCookie);



        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");
        return ResponseEntity.ok(responseBody);

    }

    }
