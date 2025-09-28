package com.imad.project.service;

import com.imad.project.controller.dto.user.LoginRequest;
import com.imad.project.model.AuthenticationResponse;

public interface ILoginService {
    AuthenticationResponse authenticate(LoginRequest loginRequest);
}
