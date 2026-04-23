package com.portal.identity_service.service;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.request.RoleRequest;
import com.portal.identity_service.dto.response.PermissionResponse;
import com.portal.identity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAll();
    RoleResponse getRoleByName(String role);
    void updateRole(String role, RoleRequest request);
    void deleteRole(String role);
}
