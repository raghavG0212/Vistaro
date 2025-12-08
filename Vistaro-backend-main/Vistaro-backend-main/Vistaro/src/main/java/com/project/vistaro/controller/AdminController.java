package com.project.vistaro.controller;

import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.UserDto;
import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;
import com.project.vistaro.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private User dtoToEntity(UserDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setCity(dto.getCity());

        if (dto.getRole() != null) {
            entity.setRole(UserRole.valueOf(dto.getRole().toUpperCase()));
        }

        return entity;
    }

    private UserDto entityToDto(User entity) {
        UserDto dto = new UserDto();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPassword(entity.getPassword());
        dto.setCity(entity.getCity());
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        return dto;
    }

    // GET user details (ADMIN only)
    @GetMapping("/user/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return entityToDto(user);
    }

    // GET user role only (ADMIN only)
    @GetMapping("/user/{id}/role")
    public String getUserRole(@PathVariable Integer id) {
        return userService.checkUserRole(id);
    }

    // UPDATE user (ADMIN only)
    @PutMapping("/user/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto dto) {
        User entity = dtoToEntity(dto);
        entity.setUserId(id);
        User updated = userService.updateUser(entity);
        return entityToDto(updated);
    }
}
