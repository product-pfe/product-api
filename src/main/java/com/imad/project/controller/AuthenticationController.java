package com.imad.project.controller;

import com.imad.project.controller.domain.AuthenticationResponseApiDto;
import com.imad.project.controller.domain.LoginRequest;
import com.imad.project.controller.domain.RegisterRequest;
import com.imad.project.mapper.UserMapper;
import com.imad.project.model.User;
import com.imad.project.service.ILoginService;
import com.imad.project.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired private IRegisterService registerService;
    @Autowired private ILoginService loginService;
    @Autowired private PasswordEncoder passwordEncoder;



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseApiDto> login(@RequestBody LoginRequest request) {
        var response = loginService.authenticate(request);
        return ResponseEntity.ok(UserMapper.toApi(response));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseApiDto> register(@RequestBody RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        String encodedConfirmPassword = passwordEncoder.encode(request.confirmPassword());
        RegisterRequest encodedRequest = new RegisterRequest(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.address(),
                request.dateOfBirth(),
                request.gender(),
                encodedPassword,
                encodedConfirmPassword
        );
        var response = registerService.saveUser(UserMapper.toDomain(encodedRequest));
        return ResponseEntity.ok(UserMapper.toApi(response));
    }
}
