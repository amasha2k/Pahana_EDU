package org.example.pahanaedu.dto;

public class ResetPasswordRequest {
    private String username;
    private String newPassword;
    private String otp;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
} 