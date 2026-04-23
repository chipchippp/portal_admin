package com.portal.identity_service.mapper;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.request.UserUpdateRequest;
import com.portal.identity_service.dto.response.PermissionResponse;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.Permission;
import com.portal.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toPermissionResponses(List<Permission> permissions);
}
