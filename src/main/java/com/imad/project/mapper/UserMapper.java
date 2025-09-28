package com.imad.project.mapper;

import com.imad.project.controller.dto.product.*;
import com.imad.project.controller.dto.user.AuthenticationResponseApiDto;
import com.imad.project.controller.dto.user.RegisterRequest;
import com.imad.project.controller.dto.user.StatusUpdateRequestDto;
import com.imad.project.controller.dto.user.UserDetailDto;
import com.imad.project.controller.dto.user.UserDto;
import com.imad.project.controller.dto.user.UserStatusDto;
import com.imad.project.model.*;

import java.time.Instant;
import java.util.List;
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
        return new StatusUpdateRequest(toDomain(dto.status()));
    }


    public static ProductDto toDto(Product p) {
        if (p == null) return null;

        CategoryDto categoryDto = null;
        if (p.category() != null) {
            // suppose les noms d'enum correspondent ; sinon adapte avec switch
            categoryDto = CategoryDto.valueOf(p.category().name());
        }

        ProductStatusDto statusDto = null;
        if (p.status() != null) {
            statusDto = ProductStatusDto.valueOf(p.status().name());
        }

        return new ProductDto(
                p.id(),
                p.ownerId(),
                p.name(),
                p.description(),
                p.price(),
                p.currency(),
                categoryDto,
                p.imageUrls() == null ? List.of() : p.imageUrls(),
                p.quantity(),
                statusDto,
                p.createdAt(),
                p.updatedAt()
        );
    }

    public static Product toDomain(ProductRequestDto dto, UUID userId) {
        return new Product(
                UUID.randomUUID(),
                userId,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.currency(),
                Category.valueOf(dto.category()),
                dto.imageUrls(),
                dto.quantity(),
                ProductStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }


}
