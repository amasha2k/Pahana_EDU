package org.example.pahanaedu.service;

import org.example.pahanaedu.repository.UserRepository;
import org.example.pahanaedu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public String register(String username, String password,String email) {
        if (userRepository.findByUsername(username) != null) {
            return "Username already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    public String forgotPassword(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(username, otp);
        // In a real app, send OTP via email/SMS. Here, return it for testing.
        return "OTP sent: " + otp;
    }

    public String resetPassword(String username, String newPassword, String otp) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        String validOtp = otpStore.get(username);
        if (validOtp == null || !validOtp.equals(otp)) {
            return "Invalid or expired OTP";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        otpStore.remove(username);
        return "Password reset successfully";
    }

    public String deleteAccount(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid password";
        }
        userRepository.delete(user);
        return "Account deleted successfully";
    }
} 