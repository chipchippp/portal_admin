package com.portal.identity_service.controller;

import com.portal.identity_service.dto.request.*;
import com.portal.identity_service.dto.response.ApiResponse;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<String> addUser(@Valid @RequestBody UserCreateRequest request){
        log.info("Request add user = {}: ", request.getUsername());
            userService.createUser(request);
            return new ApiResponse<>(HttpStatus.CREATED.value(), "user.add.success", "User created successfully");
    }


    @GetMapping("getAll")
    public ApiResponse<List<UserResponse>> getUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.warn("Username = {}: ", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.warn("Authority = {}: ", authority.getAuthority()));

        try {
            List<UserResponse> users = userService.getAllUsers();
            return new ApiResponse<>(HttpStatus.OK.value(), "Get users successfully", users);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.get.fail");
        }
    }

    @GetMapping("profile")
    public ApiResponse<UserResponse> getProfile(){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get profile successfully")
                .data(userService.getMyProfile())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id){
        try {
            UserResponse user = userService.getUserById(id);
            return new ApiResponse<>(HttpStatus.OK.value(), "user.getById.success", user);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.getById.fail");
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id,
                                                @Valid @RequestBody UserUpdateRequest request){
        try {
            UserResponse updatedUser = userService.userUpdate(id, request);
            return new ApiResponse<>(HttpStatus.OK.value(), "user.update.success", updatedUser);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.update.fail");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return new ApiResponse<>(HttpStatus.OK.value(), "user.delete.success", "User deleted successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.delete.fail", "Delete user fail");
        }
    }

}
