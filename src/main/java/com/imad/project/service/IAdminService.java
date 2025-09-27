package com.imad.project.service;

import com.imad.project.dto.UserDto;

import java.util.List;

public interface IAdminService {

    List<UserDto> getAllUsers();
}
