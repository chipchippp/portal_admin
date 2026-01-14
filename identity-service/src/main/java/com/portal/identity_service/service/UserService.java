package com.portal.identity_service.service;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.entity.User;

import java.util.List;

public interface  UserService {
    User createUser(UserCreateRequest request);
    List<User> getAllUsers();
    User getUserById(Long id);
    User userUpdate(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
}
