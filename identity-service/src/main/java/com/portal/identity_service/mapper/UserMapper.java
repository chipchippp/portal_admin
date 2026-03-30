package com.portal.identity_service.mapper;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);

    @Mapping(target = "password", ignore = true) // Tránh ánh xạ trường "status" từ UserUpdateRequest sang User
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
    void updateUserFromRequest(@MappingTarget User user, UserUpdateRequest request);
}
