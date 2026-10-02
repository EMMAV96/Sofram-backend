package com.sofram.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sofram.security.cors")
public record CorsProperties(List<String> allowedOrigins) {
}
