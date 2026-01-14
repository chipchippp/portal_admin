package com.portal.identity_service.controller;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.dto.response.ResponseData;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseData<String> addUser(@Valid @RequestBody UserCreateRequest request){
        log.info("Request add user = {}: ", request.getUsername());
        try {
            userService.createUser(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "user.add.success", "User created successfully");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "user.add.fail", "Add user fail");
        }
    }

    @GetMapping("getAll")
    public ResponseData<List<User>> getUser(){
        try {
            List<User> users = userService.getAllUsers();
            log.info("Get all users successfully, total={}", users.size());

            return new ResponseData<>(
                    HttpStatus.OK.value(),
                    "Get users successfully",
                    users
            );
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "user.get.fail");
        }
    }

    @GetMapping("/{id}")
    public ResponseData<User> getUserById(@PathVariable Long id){
        try {
            User user = userService.getUserById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "user.getById.success", user);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "user.getById.fail");
        }
    }

    @PutMapping("/{id}")
    public ResponseData<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request){
        try {
            User updatedUser = userService.userUpdate(id, request);
            return new ResponseData<>(HttpStatus.OK.value(), "user.update.success", updatedUser);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "user.update.fail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return new ResponseData<>(HttpStatus.OK.value(), "user.delete.success", "User deleted successfully");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "user.delete.fail", "Delete user fail");
        }
    }

}
