package com.imad.project.controller;

import com.imad.project.controller.dto.user.StatusUpdateRequestDto;
import com.imad.project.controller.dto.user.UserDetailDto;
import com.imad.project.controller.dto.user.UserDto;
import com.imad.project.mapper.UserMapper;
import com.imad.project.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminController {

    @Autowired private IAdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAllUser (){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserDetailDto> getUserById (@PathVariable UUID id){
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/users/{id}/status")
    public ResponseEntity<Void> updateUserStatus (@PathVariable UUID id, @RequestBody StatusUpdateRequestDto statusUpdateRequest){
        adminService.updateUserStatus(id, UserMapper.toDomain(statusUpdateRequest));
        return ResponseEntity.noContent().build();
    }

}
