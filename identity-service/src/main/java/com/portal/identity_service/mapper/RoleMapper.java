package com.portal.identity_service.mapper;

import com.portal.identity_service.dto.request.RoleRequest;
import com.portal.identity_service.dto.response.RoleResponse;
import com.portal.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toRoleResponses(List<Role> roles);
}
