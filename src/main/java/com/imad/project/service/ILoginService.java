package com.imad.project.service;

import com.imad.project.controller.domain.LoginRequest;
import com.imad.project.model.AuthenticationResponse;

public interface ILoginService {
    AuthenticationResponse authenticate(LoginRequest loginRequest);
}
