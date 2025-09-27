package com.imad.project.config;

import com.imad.project.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService implements IJwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // en ms (ou la valeur que tu utilises)

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration; // en ms

    private static final String CLAIM_USER_ID = "uid";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLES = "roles";

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> extra = new HashMap<>();
        if (user.getId() != null) extra.put(CLAIM_USER_ID, user.getId().toString());
        if (user.getEmail() != null) extra.put(CLAIM_EMAIL, user.getEmail());
        if (user.getRole() != null) extra.put(CLAIM_ROLES, List.of(user.getRole()));
        return buildToken(extra, user, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(Collections.emptyMap(), userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails, UUID userId, String email, List<String> roles) {
        Map<String, Object> extra = new HashMap<>();
        if (userId != null) extra.put(CLAIM_USER_ID, userId.toString());
        if (email != null) extra.put(CLAIM_EMAIL, email);
        if (roles != null) extra.put(CLAIM_ROLES, roles);
        return buildToken(extra, userDetails, refreshExpiration);
    }

    private String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationMillis) {
        final Date now = new Date(System.currentTimeMillis());
        final Date expiry = new Date(System.currentTimeMillis() + expirationMillis);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp.before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object uid = claims.get(CLAIM_USER_ID);
        if (uid == null) return null;
        try {
            return UUID.fromString(uid.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        Object email = claims.get(CLAIM_EMAIL);
        return email != null ? email.toString() : null;
    }

    @Override
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get(CLAIM_ROLES);
        if (rolesObj == null) return Collections.emptyList();

        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        String s = rolesObj.toString();
        if (s.isBlank()) return Collections.emptyList();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
    }
}