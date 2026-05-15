package com.movieflix.service.impl;

import com.movieflix.entity.OtpPurpose;
import com.movieflix.entity.OtpVerification;
import com.movieflix.repository.OtpVerificationRepository;
import com.movieflix.service.EmailService;
import com.movieflix.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailService emailService;

    @Override
    public void generateAndSendOtp(String email, OtpPurpose purpose) {
        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        OtpVerification entry = new OtpVerification();
        entry.setEmail(email.toLowerCase());
        entry.setOtp(otp);
        entry.setPurpose(purpose);
        entry.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpVerificationRepository.save(entry);
        emailService.sendOtp(email, otp, purpose.name());
    }

    @Override
    public boolean validateOtp(String email, OtpPurpose purpose, String otp) {
        return otpVerificationRepository.findFirstByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email.toLowerCase(), purpose)
                .filter(entry -> entry.getExpiresAt().isAfter(LocalDateTime.now()))
                .filter(entry -> entry.getOtp().equals(otp))
                .map(entry -> {
                    entry.setUsed(true);
                    otpVerificationRepository.save(entry);
                    return true;
                })
                .orElse(false);
    }
}
