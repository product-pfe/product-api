package com.imad.project.service;

import com.imad.project.dto.UserDetailDto;
import com.imad.project.dto.UserDto;
import com.imad.project.mapper.UserMapper;
import com.imad.project.model.Role;
import com.imad.project.model.StatusUpdateRequest;
import com.imad.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserDetailsDto)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Override
    public void updateUserStatus(UUID id, StatusUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("StatusUpdateRequest cannot be null");
        }

        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        var newStatus = request.status();

        if (newStatus == null) {
            throw new IllegalArgumentException("Status must be provided");
        }

        user.setStatus(newStatus);
        userRepository.save(user);
    }
}
