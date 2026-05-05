package com.portal.identity_service.service;

import java.util.List;

import com.portal.identity_service.dto.request.RoleRequest;
import com.portal.identity_service.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAll();

    RoleResponse getRoleByName(String role);

    void updateRole(String role, RoleRequest request);

    void deleteRole(String role);
}
