package com.portal.identity_service.mapper;

import java.util.List;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.response.PermissionResponse;
import com.portal.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponses(List<Permission> permissions);
}
