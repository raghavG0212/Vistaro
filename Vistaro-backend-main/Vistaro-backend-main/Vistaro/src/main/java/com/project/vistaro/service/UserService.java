package com.project.vistaro.service;

import java.util.List;
import com.project.vistaro.model.User;

public interface UserService {
	 User addUser(User user);
	 User updateUser(User user);
	 User getUserById(Integer id);
	 String checkUserRole(Integer id);
	 User getUserByEmail(String email);
	 boolean checkUserByEmail(String email);
	 boolean checkUserByPhone(String phone);

}
