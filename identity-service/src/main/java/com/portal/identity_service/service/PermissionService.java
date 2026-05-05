package com.portal.identity_service.service;

import java.util.List;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    PermissionResponse getPermissionByName(String permission);

    void updatePermission(String permission, PermissionRequest request);

    void deletePermission(String permission);
}
