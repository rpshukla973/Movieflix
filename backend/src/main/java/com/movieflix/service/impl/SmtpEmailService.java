package com.movieflix.service.impl;

import com.movieflix.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String toEmail, String otp, String purpose) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Movieflix OTP - " + purpose);
        message.setText("Your OTP is: " + otp + "\nThis OTP will expire shortly.");
            try {
                mailSender.send(message);
            } catch (Exception e) {
                // For development/testing: log the OTP instead of sending email
                System.out.println("=== DEVELOPMENT MODE - OTP GENERATED ===");
                System.out.println("Email: " + toEmail);
                System.out.println("OTP: " + otp);
                System.out.println("Purpose: " + purpose);
                System.out.println("==========================================");
            }
    }
}
