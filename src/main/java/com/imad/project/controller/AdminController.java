package com.imad.project.controller;

import com.imad.project.controller.domain.RegisterRequest;
import com.imad.project.dto.StatusUpdateRequestDto;
import com.imad.project.dto.UserDetailDto;
import com.imad.project.dto.UserDto;
import com.imad.project.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminController {

    @Autowired private IAdminService adminService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAllUser (){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserDetailDto> getUserById (@PathVariable UUID id){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PatchMapping("/admin/users/{id}/status")
    public ResponseEntity<Void> updateUserStatus (@PathVariable UUID id, @RequestBody StatusUpdateRequestDto statusUpdateRequest){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

}
