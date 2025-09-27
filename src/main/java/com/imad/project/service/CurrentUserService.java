package com.imad.project.service;

import com.imad.project.config.IJwtService;
import com.imad.project.exception.ProductException;
import com.imad.project.utils.ServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserService implements ICurrentUserService {

    private final IJwtService jwtService;

    public CurrentUserService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public UUID getUserId() {
        String token = token();
        UUID userId = jwtService.extractUserId(token);
        if (userId == null) {
            throw new ProductException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return userId;
    }

    @Override
    public String getEmail() {
        String token = token();
        String email = jwtService.extractEmail(token);
        if (email == null) {
            throw new ProductException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return email;
    }

    private String token() {
        String header = ServletUtil.getRequest().getHeader("Authorization");
        if (header == null || header.isBlank()) {
            throw new ProductException(HttpStatus.UNAUTHORIZED, "Missing token");
        }
        final String prefix = "Bearer ";
        if (header.startsWith(prefix)) {
            return header.substring(prefix.length());
        } else {
            throw new ProductException(HttpStatus.UNAUTHORIZED, "Invalid Authorization Header");
        }
    }
}