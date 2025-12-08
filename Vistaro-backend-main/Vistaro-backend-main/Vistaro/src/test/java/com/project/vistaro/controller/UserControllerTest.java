package com.project.vistaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.UserDto;
import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;
import com.project.vistaro.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    // ------------------------------
    // Helper Method
    // ------------------------------
    private User createUser() {
        User u = new User();
        u.setUserId(1);
        u.setName("John Doe");
        u.setEmail("john@example.com");
        u.setPhone("9999999999");
        u.setPassword("secret");
        u.setCity("Mumbai");
        u.setRole(UserRole.USER);
        return u;
    }

    private UserDto createDto() {
        UserDto dto = new UserDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPhone("9999999999");
        dto.setPassword("secret");
        dto.setCity("Mumbai");
        dto.setRole("USER");
        return dto;
    }

    // ------------------------------
    // TEST: Add User
    // ------------------------------
    @Test
    void testAddUser() throws Exception {

        User saved = createUser();
        UserDto dto = createDto();

        when(userService.addUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    // ------------------------------
    // TEST: Update User
    // ------------------------------
    @Test
    void testUpdateUser() throws Exception {

        User updated = createUser();
        updated.setCity("Delhi");

        UserDto dto = createDto();
        dto.setCity("Delhi");

        when(userService.updateUser(any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Delhi"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    // ------------------------------
    // TEST: Get User by ID
    // ------------------------------
    @Test
    void testGetUserById() throws Exception {

        when(userService.getUserById(1)).thenReturn(createUser());

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
