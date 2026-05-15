package com.movieflix.controller;

import com.movieflix.dto.auth.*;
import com.movieflix.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/otp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestOtp(@Valid @RequestBody OtpRequest request) {
        authService.requestOtp(request);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/login/otp")
    public TokenResponse loginWithOtp(@Valid @RequestBody LoginOtpRequest request) {
        return authService.loginWithOtp(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/forgot-password/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ForgotPasswordResetRequest request) {
        authService.resetPassword(request);
    }
}
