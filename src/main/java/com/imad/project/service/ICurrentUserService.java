package com.imad.project.service;

import java.util.UUID;

public interface ICurrentUserService {
    UUID getUserId();
    String getEmail();

    boolean hasRole(String role);
    boolean hasAnyRole(String... roles);
    public boolean isAdmin();
}