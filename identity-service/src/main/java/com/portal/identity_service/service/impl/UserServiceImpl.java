package com.portal.identity_service.service.impl;

import java.util.HashSet;
import java.util.List;

import com.portal.identity_service.constant.PredefinedRole;
import com.portal.identity_service.dto.request.ProfileCreateRequest;
import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.dto.response.UserProfileResponse;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.Role;
import com.portal.identity_service.entity.User;

import com.portal.identity_service.excetion.*;
import com.portal.identity_service.mapper.ProfileMapper;
import com.portal.identity_service.mapper.UserMapper;
import com.portal.identity_service.repository.RoleRepository;
import com.portal.identity_service.repository.UserRepository;
import com.portal.identity_service.repository.httpClient.ProfileClient;
import com.portal.identity_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @Transactional
    @Override
    public UserResponse createUser(UserCreateRequest request) {

        log.info("Create username = {}", request.getUsername());

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE)
                .ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        ProfileCreateRequest profileRequest =
                profileMapper.toProfileCreateRequest(request);

        profileRequest.setUserId(String.valueOf(user.getId()));

        try {
            UserProfileResponse profileResponse =
                    profileClient.createProfile(profileRequest);

            log.info("Profile created: {}", profileResponse);

        } catch (Exception e) {

            log.error("Create profile failed", e);

            throw new RuntimeException(
                    "User created but profile creation failed"
            );
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse userUpdate(Long id, UserUpdateRequest request) {
        User user = getUserEntityById(id);
        userMapper.updateUserFromRequest(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }

    @Override
    public UserResponse getMyProfile() {

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse response =
                userMapper.toUserResponse(user);

        try {
            UserProfileResponse profile =
                    profileClient.getProfile(
                            String.valueOf(user.getId()));

            response.setFullName(profile.getFullName());
            response.setDateOfBirth(profile.getDateOfBirth());

        } catch (Exception e) {
            log.error("Cannot get profile", e);
        }

        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("hasAuthority('APPROVE_POST')")
    @Override
    public List<UserResponse> getAllUsers() {
        log.warn("in method get users");
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse response = userMapper.toUserResponse(user);

        UserProfileResponse profile =
                profileClient.getProfile(String.valueOf(id));

        response.setFullName(profile.getFullName());
        response.setDateOfBirth(profile.getDateOfBirth());

        return response;
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
