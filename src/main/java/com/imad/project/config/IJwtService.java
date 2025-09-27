package com.imad.project.config;

import com.imad.project.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface IJwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver);

    String generateToken(User user);


    String generateRefreshToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails, UUID userId, String email, List<String> roles);

    boolean isTokenValid(String token, UserDetails userDetails);

    UUID extractUserId(String token);

    String extractEmail(String token);

    List<String> extractRoles(String token);
}