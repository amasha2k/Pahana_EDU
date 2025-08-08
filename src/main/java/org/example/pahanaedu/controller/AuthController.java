package org.example.pahanaedu.controller;

import org.example.pahanaedu.service.AuthService;
import org.example.pahanaedu.dto.RegisterRequest;
import org.example.pahanaedu.dto.ForgotPasswordRequest;
import org.example.pahanaedu.dto.ResetPasswordRequest;
import org.example.pahanaedu.dto.DeleteAccountRequest;
import org.example.pahanaedu.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:14192")
@RestController
@RequestMapping(Routes.AUTH_BASE)
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(Routes.LOGIN)
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        boolean authenticated = authService.authenticate(username, password);
        Map<String, Object> response = new HashMap<>();
        response.put("success", authenticated);
        if (authenticated) {
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping(Routes.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String result = authService.register(request.getUsername(), request.getPassword(),request.getEmail());
        boolean success = result.equals("User registered successfully");
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", result);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(Routes.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String result = authService.forgotPassword(request.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        boolean success = result.contains("sent");
        response.put("success", success);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(Routes.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        String result = authService.resetPassword(request.getUsername(), request.getNewPassword(), request.getOtp());
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        boolean success = result.contains("successfully");
        response.put("success", success);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping(Routes.DELETE_ACCOUNT)
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest request) {
        String result = authService.deleteAccount(request.getUsername(), request.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        boolean success = result.contains("successfully");
        response.put("success", success);
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(Routes.LOGOUT)
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }
} 