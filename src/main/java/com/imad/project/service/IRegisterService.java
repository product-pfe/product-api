package com.imad.project.service;

import com.imad.project.model.AuthenticationResponse;
import com.imad.project.model.User;

public interface IRegisterService {

    AuthenticationResponse saveUser(User user);
}
