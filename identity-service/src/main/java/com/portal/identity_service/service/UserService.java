package com.portal.identity_service.service;

import java.util.List;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse userUpdate(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    UserResponse getMyProfile();
}
