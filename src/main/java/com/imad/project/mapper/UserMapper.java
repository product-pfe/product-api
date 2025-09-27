package com.imad.project.mapper;

import com.imad.project.controller.domain.AuthenticationResponseApiDto;
import com.imad.project.controller.domain.RegisterRequest;
import com.imad.project.dto.StatusUpdateRequestDto;
import com.imad.project.dto.UserDetailDto;
import com.imad.project.dto.UserDto;
import com.imad.project.dto.UserStatusDto;
import com.imad.project.model.*;

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
                Status.PENDING,
                Role.USER
        );
    }

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getStatus());
    }

    public static UserDetailDto toUserDetailsDto(User user) {
        return new UserDetailDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getStatus()
        );
    }

    public static AuthenticationResponseApiDto toApi(AuthenticationResponse response) {
        return new AuthenticationResponseApiDto(
                response.accessToken(), response.refreshToken()
        );
    }

    public static Status toDomain(UserStatusDto dto) {
        if (dto == null) {
            return null;
        }

        return switch (dto) {
            case PENDING  -> Status.PENDING;
            case ACCEPTED -> Status.ACCEPTED;
            case REJECTED -> Status.REJECTED;
            case DELETED  -> Status.DELETED;
            default -> throw new IllegalArgumentException("Unknown UserStatusDto: " + dto);
        };
    }

    public static StatusUpdateRequest toDomain(StatusUpdateRequestDto dto) {
        return new StatusUpdateRequest(toDomain(dto.userStatusDto()));
    }
}
