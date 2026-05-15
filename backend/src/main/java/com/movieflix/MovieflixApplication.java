package com.movieflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieflixApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieflixApplication.class, args);
    }
}
