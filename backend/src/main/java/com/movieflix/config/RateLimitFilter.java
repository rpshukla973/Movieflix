package com.movieflix.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 10;
    private static final long WINDOW_SECONDS = 60;
    private final Map<String, AttemptWindow> attempts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().contains("/api/auth/login")) {
            String key = request.getRemoteAddr();
            AttemptWindow window = attempts.computeIfAbsent(key, k -> new AttemptWindow(0, Instant.now().getEpochSecond()));
            long now = Instant.now().getEpochSecond();

            if (now - window.windowStart > WINDOW_SECONDS) {
                attempts.put(key, new AttemptWindow(1, now));
            } else if (window.count >= MAX_ATTEMPTS) {
                response.setStatus(429);
                response.getWriter().write("Too many login attempts. Try again later.");
                return;
            } else {
                attempts.put(key, new AttemptWindow(window.count + 1, window.windowStart));
            }
        }

        filterChain.doFilter(request, response);
    }

    private record AttemptWindow(int count, long windowStart) {
    }
}
