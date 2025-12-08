package com.project.vistaro.repository;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;

@Repository
public class UserDaoImp implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));
            user.setCity(rs.getString("city"));

            String roleStr = rs.getString("role");
            if (roleStr != null) {
                user.setRole(UserRole.valueOf(roleStr));
            }

            if (rs.getTimestamp("created_at") != null) {
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return user;
        }
    };

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO user (name, email, phone, password, city, role) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getRole() != null ? user.getRole().name() : "USER");
            return ps;
        }, keyHolder);

        // ⭐ set generated user_id back into object
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            user.setUserId(generatedId.intValue());
        }

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE user SET name = ?, email = ?, phone = ?, password = ?, city = ?, role = ? WHERE user_id = ?";

        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getCity(),
                user.getRole().name(),
                user.getUserId()
        );
        return user;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        String sql = "SELECT * FROM user WHERE user_id = ?"; // <-- fixed table name
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    @Override
    public String checkUserRole(Integer id) {
        String sql = "SELECT role FROM user WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email).stream().findFirst();
    }

	@Override
	public Optional<User> getUserByPhone(String phone) {
		String sql = "SELECT * FROM user WHERE phone = ?";
        return jdbcTemplate.query(sql, userRowMapper, phone).stream().findFirst();
	}
}
