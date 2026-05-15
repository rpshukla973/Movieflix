package com.movieflix.service.impl;

import com.movieflix.dto.auth.*;
import com.movieflix.entity.*;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.RefreshTokenRepository;
import com.movieflix.repository.RoleRepository;
import com.movieflix.repository.UserRepository;
import com.movieflix.security.JwtTokenProvider;
import com.movieflix.service.AuthService;
import com.movieflix.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void requestOtp(OtpRequest request) {
        otpService.generateAndSendOtp(request.email(), request.purpose());
    }

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.email().toLowerCase()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
        }
        boolean otpValid = otpService.validateOtp(request.email(), OtpPurpose.SIGNUP, request.otp());
        if (!otpValid) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        User user = new User();
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerified(true);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
        );
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        return issueTokens(user, authentication);
    }

    @Override
    public TokenResponse loginWithOtp(LoginOtpRequest request) {
        boolean otpValid = otpService.validateOtp(request.email(), OtpPurpose.LOGIN, request.otp());
        if (!otpValid) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        if (!user.isActive()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "User is inactive");
        }

        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        return issueTokens(user, authentication);
    }

    private TokenResponse issueTokens(User user, Authentication authentication) {
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(accessToken, refreshToken.getToken(), "Bearer", jwtTokenProvider.getAccessTokenTtlMs());
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .filter(token -> token.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(refreshToken.getUser().getEmail(), null);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        return new TokenResponse(accessToken, refreshToken.getToken(), "Bearer", jwtTokenProvider.getAccessTokenTtlMs());
    }

    @Override
    @Transactional
    public void resetPassword(ForgotPasswordResetRequest request) {
        boolean otpValid = otpService.validateOtp(request.email(), OtpPurpose.PASSWORD_RESET, request.otp());
        if (!otpValid) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
