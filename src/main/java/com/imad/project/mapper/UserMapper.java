package com.imad.project.mapper;

import com.imad.project.controller.domain.AuthenticationResponseApiDto;
import com.imad.project.controller.domain.RegisterRequest;
import com.imad.project.dto.UserDto;
import com.imad.project.model.AuthenticationResponse;
import com.imad.project.model.Role;
import com.imad.project.model.User;
import com.imad.project.model.UserStatus;

import java.util.UUID;

public class UserMapper {

    public static User toDomain(RegisterRequest request) {
        return new User(
                UUID.randomUUID(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.address(),
                request.dateOfBirth(),
                request.gender(),
                request.password(),
                UserStatus.PENDING,
                Role.USER
        );
    }

    public static UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getUserStatus());
    }

    public static AuthenticationResponseApiDto toApi(AuthenticationResponse response) {
        return new AuthenticationResponseApiDto(
                response.accessToken(), response.refreshToken()
        );
    }
}
