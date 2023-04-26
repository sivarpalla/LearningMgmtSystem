package com.mks.lms.service;

import java.util.List;

import com.mks.lms.entity.UserEntity;
import com.mks.lms.model.User;
import com.mks.lms.model.ValidateUser;
import com.mks.lms.model.request.ChangePasswordRequest;
import com.mks.lms.model.request.SignupRequest;

import com.mks.lms.model.response.ChangePasswordResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	UserEntity createUser(User user);
	UserEntity updateUser(Integer userId, User user);
    void deleteUser(Integer userId);
    User getUserById(Integer userId);
    List<User> getAllUsers(int page, int size);
	boolean validateUser(ValidateUser user);
	List<User> searchByKeyword(String keyword);

	Boolean existsByEmail(String email);

	UserEntity createUser(SignupRequest user);
	
	void importUser(MultipartFile excel);
	ChangePasswordResponse changePassword(ChangePasswordRequest request);
}
