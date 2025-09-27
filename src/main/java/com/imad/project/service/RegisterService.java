package com.imad.project.service;

import com.imad.project.config.IJwtService;
import com.imad.project.config.JwtService;
import com.imad.project.model.AuthenticationResponse;
import com.imad.project.model.Token;
import com.imad.project.model.User;
import com.imad.project.repository.ITokenRepository;
import com.imad.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements IRegisterService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITokenRepository tokenRepository;


    @Autowired
    private IJwtService jwtService;

    @Override
    public AuthenticationResponse saveUser(User user) {
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token(
                user.getId(),
                jwtToken
        );
        tokenRepository.save(token);
    }
}

