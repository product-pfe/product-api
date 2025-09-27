package com.imad.project.service;

import java.util.UUID;

public interface ICurrentUserService {
    UUID getUserId();
    String getEmail();
}