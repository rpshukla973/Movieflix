package com.movieflix.service;

public interface EmailService {
    void sendOtp(String toEmail, String otp, String purpose);
}
