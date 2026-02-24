package com.cook.cancook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for Spring Boot
 * Enables CORS for the React/HTML frontend (both local and production)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/api/**")
        // Allow requests from frontend (both local development and GitHub Pages)
        .allowedOrigins(
            "http://localhost:3000", // React dev server
            "http://localhost:5500", // Live Server default
            "http://127.0.0.1:5500", // Live Server alternative
            "http://localhost:8000", // Python HTTP server
            "https://softwarecancook.github.io" // GitHub Pages production
            )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
