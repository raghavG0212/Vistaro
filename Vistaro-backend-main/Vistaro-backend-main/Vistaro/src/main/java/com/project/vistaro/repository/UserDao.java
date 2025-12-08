package com.project.vistaro.repository;
import java.util.Optional;
import com.project.vistaro.model.User;

public interface UserDao {
	 User addUser(User user);
	 User updateUser(User user);
	 Optional<User> getUserById(Integer id);
	 String checkUserRole(Integer id);
	 Optional<User> getUserByEmail(String email);
	 Optional<User> getUserByPhone(String phone);
}
