package com.movieflix.service.impl;

import com.movieflix.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String toEmail, String otp, String purpose) {
        // Always log OTP for development visibility (safe for non-production)
        log.info("Generated OTP for {} (purpose={}): {}", toEmail, purpose, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("shuklabrothersgroup@gmail.com");
        message.setSubject("Movieflix OTP - " + purpose);
        message.setText("Your OTP is: " + otp + "\nThis OTP will expire shortly.");
        try {
            mailSender.send(message);
            log.debug("OTP email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send OTP email to {} - falling back to logged OTP", toEmail, e);
        }
    }
}
