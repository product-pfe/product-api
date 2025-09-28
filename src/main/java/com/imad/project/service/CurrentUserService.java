package com.imad.project.service;

import com.imad.project.config.IJwtService;
import com.imad.project.exception.ProductException;
import com.imad.project.utils.ServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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

    @Override
    public boolean hasRole(String role) {
        if (role == null || role.isBlank()) return false;
        String normalizedRequired = normalizeRole(role);

        String token = token(); // will throw 401 if missing/invalid header
        List<String> tokenRoles = jwtService.extractRoles(token);
        if (tokenRoles == null || tokenRoles.isEmpty()) return false;

        return tokenRoles.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeRole)
                .anyMatch(r -> r.equalsIgnoreCase(normalizedRequired));
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        if (roles == null) return false;
        for (String r : roles) {
            if (hasRole(r)) return true;
        }
        return false;
    }

    @Override
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    private String normalizeRole(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toUpperCase(Locale.ROOT);
        if (s.startsWith("ROLE_")) return s.substring("ROLE_".length());
        return s;
    }
}