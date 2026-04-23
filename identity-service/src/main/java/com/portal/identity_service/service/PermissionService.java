package com.portal.identity_service.service;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermissions();
    PermissionResponse getPermissionByName(String permission);
    void updatePermission(String permission, PermissionRequest request);
    void deletePermission(String permission);
}
