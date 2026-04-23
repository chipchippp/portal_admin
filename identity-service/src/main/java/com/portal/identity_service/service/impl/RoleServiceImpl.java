package com.portal.identity_service.service.impl;

import com.portal.identity_service.dto.request.RoleRequest;
import com.portal.identity_service.dto.response.RoleResponse;
import com.portal.identity_service.mapper.RoleMapper;
import com.portal.identity_service.repository.PermissionRepository;
import com.portal.identity_service.repository.RoleRepository;
import com.portal.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;


    @Override
    public RoleResponse createRole(RoleRequest request) {

        if(roleRepository.existsById(request.getName())){
            throw new RuntimeException("Role already exists");
        }

        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());

        if(permissions.size() != request.getPermissions().size()){
            throw new RuntimeException("Some permissions not found");
        }

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse getRoleByName(String role) {
        return null;
    }

    @Override
    public void updateRole(String role, RoleRequest request) {

    }

    @Override
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
