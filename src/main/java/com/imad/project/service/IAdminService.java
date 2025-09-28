package com.imad.project.service;

import com.imad.project.controller.dto.user.UserDetailDto;
import com.imad.project.controller.dto.user.UserDto;
import com.imad.project.model.StatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface IAdminService {

    List<UserDto> getAllUsers();
    UserDetailDto getUserById(UUID id);
    void updateUserStatus(UUID id, StatusUpdateRequest request);
}
