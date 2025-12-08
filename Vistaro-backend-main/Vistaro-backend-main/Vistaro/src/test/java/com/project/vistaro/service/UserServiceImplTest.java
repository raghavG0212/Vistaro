package com.project.vistaro.service;
 
import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;
import com.project.vistaro.repository.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
class UserServiceImplTest {
 
    @Mock
    private UserDao userDao;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @InjectMocks
    private UserServiceImpl userService;
 
    private User user;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("plainPassword");
        user.setPhone("1234567890");
        user.setCity("New York");
        user.setRole(UserRole.USER);
    }
 
    @Test
    void testAddUser_ShouldEncodePasswordAndSave() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userDao.addUser(any(User.class))).thenReturn(user);
 
        User savedUser = userService.addUser(user);
 
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userDao, times(1)).addUser(any(User.class));
    }
 
    @Test
    void testUpdateUser_ShouldUpdateFields() {
        User updatedUser = new User();
        updatedUser.setUserId(1);
        updatedUser.setName("Jane Doe");
        updatedUser.setPhone("9876543210");
        updatedUser.setCity("Los Angeles");
        updatedUser.setPassword("newPassword");
        updatedUser.setRole(UserRole.ADMIN); // ✅ use enum
 
        when(userDao.getUserById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userDao.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
 
        User result = userService.updateUser(updatedUser);
 
        assertEquals("Jane Doe", result.getName());
        assertEquals("9876543210", result.getPhone());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals(UserRole.ADMIN, result.getRole()); // ✅ enum comparison
    }
 
 
    @Test
    void testUpdateUser_ShouldThrowException_WhenUserNotFound() {
        User updatedUser = new User();
        updatedUser.setUserId(99);
 
        when(userDao.getUserById(99)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(updatedUser));
    }
 
    @Test
    void testGetUserById_ShouldReturnUser() {
        when(userDao.getUserById(1)).thenReturn(Optional.of(user));
 
        User result = userService.getUserById(1);
 
        assertEquals("John Doe", result.getName());
        verify(userDao, times(1)).getUserById(1);
    }
 
    @Test
    void testGetUserById_ShouldThrowException_WhenNotFound() {
        when(userDao.getUserById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
    }
 
    @Test
    void testCheckUserRole_ShouldReturnRole() {
        when(userDao.checkUserRole(1)).thenReturn("USER");
 
        String role = userService.checkUserRole(1);
 
        assertEquals("USER", role);
        verify(userDao, times(1)).checkUserRole(1);
    }
 
    @Test
    void testGetUserByEmail_ShouldReturnUser() {
        when(userDao.getUserByEmail("john@example.com")).thenReturn(Optional.of(user));
 
        User result = userService.getUserByEmail("john@example.com");
 
        assertEquals("John Doe", result.getName());
        verify(userDao, times(1)).getUserByEmail("john@example.com");
    }
 
    @Test
    void testGetUserByEmail_ShouldThrowException_WhenNotFound() {
        when(userDao.getUserByEmail("missing@example.com")).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("missing@example.com"));
    }
}