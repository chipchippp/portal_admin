package com.portal.identity_service.service.impl;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.response.PermissionResponse;
import com.portal.identity_service.entity.Permission;
import com.portal.identity_service.mapper.PermissionMapper;
import com.portal.identity_service.repository.PermissionRepository;
import com.portal.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();

    }

    @Override
    public PermissionResponse getPermissionByName(String permission) {
        return null;
    }

    @Override
    public void updatePermission(String permission, PermissionRequest request) {

    }

    @Override
    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }

}
