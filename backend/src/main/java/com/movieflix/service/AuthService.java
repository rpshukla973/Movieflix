package com.movieflix.service;

import com.movieflix.dto.auth.*;

public interface AuthService {
    void requestOtp(OtpRequest request);
    void signup(SignupRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse loginWithOtp(LoginOtpRequest request);
    TokenResponse refresh(RefreshTokenRequest request);
    void resetPassword(ForgotPasswordResetRequest request);
}
