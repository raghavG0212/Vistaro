package com.project.vistaro.repository;
 
import com.project.vistaro.model.User;
import org.springframework.jdbc.support.KeyHolder;
import com.project.vistaro.model.UserRole;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
 
import java.util.List;
import java.util.Optional;
 
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.PreparedStatementCreator;
class UserDaoImplTest {
 
    @Mock
    private JdbcTemplate jdbcTemplate;
 
    @InjectMocks
    private UserDaoImp userDao;
 
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
 
    // Helper to create sample user
    private User createUser() {
        User user = new User();
        user.setUserId(1);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPhone("9999999999");
        user.setPassword("secret");
        user.setCity("Delhi");
        user.setRole(UserRole.USER);
        return user;
    }
 
    // ----------------------------------------
    // TEST: addUser()
    // ----------------------------------------
    @Test
    void testAddUser() {
        User user = createUser();
        // Stub the correct overload: update(PreparedStatementCreator, KeyHolder)
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenReturn(1);
        User result = userDao.addUser(user);
        assertEquals("John", result.getName());
        // Verify the correct overload was called
        verify(jdbcTemplate, times(1))
                .update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }
    // ----------------------------------------
    // TEST: updateUser()
    // ----------------------------------------
    @Test
    void testUpdateUser() {
        User user = createUser();
        // Stub with the extra "USER" role argument
        when(jdbcTemplate.update(anyString(),
                eq(user.getName()), eq(user.getEmail()), eq(user.getPhone()),
                eq(user.getPassword()), eq(user.getCity()), eq("USER"), eq(user.getUserId())
        )).thenReturn(1);
        User result = userDao.updateUser(user);
        assertEquals("John", result.getName());
        // Verify with the role included
        verify(jdbcTemplate, times(1))
                .update(anyString(),
                        eq(user.getName()), eq(user.getEmail()), eq(user.getPhone()),
                        eq(user.getPassword()), eq(user.getCity()), eq("USER"), eq(user.getUserId()));
    }
 
    // ----------------------------------------
    // TEST: getUserById() → Found
    // ----------------------------------------
    @Test
    void testGetUserByIdFound() {
        User user = createUser();
 
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of(user));
 
        Optional<User> result = userDao.getUserById(1);
 
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getUserId());
        assertEquals("John", result.get().getName());
    }
 
    // ----------------------------------------
    // TEST: getUserById() → Not Found
    // ----------------------------------------
    @Test
    void testGetUserByIdNotFound() {
 
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of());
 
        Optional<User> result = userDao.getUserById(1);
 
        assertTrue(result.isEmpty());
    }
}