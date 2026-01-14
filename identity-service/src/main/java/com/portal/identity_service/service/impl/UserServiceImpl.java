package com.portal.identity_service.service.impl;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.repository.UserRepository;
import com.portal.identity_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .status(request.getStatus())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User userUpdate(Long id, UserUpdateRequest request) {
        User user = getUserById(id);
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setStatus(request.getStatus());
        user.setDateOfBirth(request.getDateOfBirth());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow((() -> new RuntimeException("User not found")));
    }

}
