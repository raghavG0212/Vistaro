package com.project.vistaro.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.vistaro.model.User;
import com.project.vistaro.model.UserRole;

public class UserRowMapper implements RowMapper<User> {
	@Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setCity(rs.getString("city"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        return user;
    }

};