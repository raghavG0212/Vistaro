package com.project.vistaro.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.User;
import com.project.vistaro.repository.UserDao;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.addUser(user);
    }

    @Override
    public User updateUser(User updatedUser) {

        // 1. Fetch existing user from DB
        User existing = userDao.getUserById(updatedUser.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updatedUser.getUserId()));

        // 2. Update allowed fields
        existing.setName(updatedUser.getName());
        existing.setPhone(updatedUser.getPhone());
        existing.setCity(updatedUser.getCity());

        // 3. Update password ONLY if user entered a new one
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // 4. Update role only if provided (optional)
        if (updatedUser.getRole() != null) {
            existing.setRole(updatedUser.getRole());
        }

        // 5. Save and return
        return userDao.updateUser(existing);
    }


    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));
    }

    @Override
    public String checkUserRole(Integer id) {
        return userDao.checkUserRole(id);
    }

	@Override
	public boolean checkUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.getUserByEmail(email).isPresent();
	}

	@Override
	public boolean checkUserByPhone(String phone) {
		// TODO Auto-generated method stub
		return userDao.getUserByPhone(phone).isPresent();
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.getUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with this email."));
	}

    
}
