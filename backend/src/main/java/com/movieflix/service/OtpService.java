package com.movieflix.service;

import com.movieflix.entity.OtpPurpose;

public interface OtpService {
    void generateAndSendOtp(String email, OtpPurpose purpose);
    boolean validateOtp(String email, OtpPurpose purpose, String otp);
}
