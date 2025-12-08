package com.project.vistaro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.vistaro.dto.UserDto;
import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;
import com.project.vistaro.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    private User dtoToEntity(UserDto dto) {
        User entity = new User();
        //entity.setUserId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setCity(dto.getCity());

        if (dto.getRole() != null) {
            try {
                entity.setRole(UserRole.valueOf(dto.getRole().toUpperCase())); 
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + dto.getRole());
            }
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
    // REST endpoints
    @PostMapping
    public UserDto addUser(@RequestBody UserDto dto) {
        return entityToDto(userService.addUser(dtoToEntity(dto)));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto dto) {
        User entity = dtoToEntity(dto);
        entity.setUserId(id);
        return entityToDto(userService.updateUser(entity));
    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);   // service returns User directly
        return entityToDto(user); 
    }
    
}
