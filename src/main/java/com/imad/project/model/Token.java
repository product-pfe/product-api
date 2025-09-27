package com.imad.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("tokens")
public class Token {

    @Id
    public UUID id = UUID.randomUUID();
    public UUID userId;
    public String token;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked = false;
    public boolean expired = false;

    public Token() {
    }

    public Token(UUID userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
