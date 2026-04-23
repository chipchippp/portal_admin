package com.portal.identity_service.controller;

import com.portal.identity_service.dto.request.RoleRequest;
import com.portal.identity_service.dto.response.ApiResponse;
import com.portal.identity_service.dto.response.RoleResponse;
import com.portal.identity_service.service.PermissionService;
import com.portal.identity_service.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> addRole(@Valid @RequestBody RoleRequest request){
            return ApiResponse.<RoleResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("permission.create.success")
                    .data(roleService.createRole(request))
                    .build();
    }


    @GetMapping("getAll")
    public ApiResponse<List<RoleResponse>> getAll(){
        try {
            List<RoleResponse> role = roleService.getAll();
            return new ApiResponse<>(HttpStatus.OK.value(), "Get users successfully", role);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.get.fail");
        }
    }


    @DeleteMapping("/{role}")
    public ApiResponse<String> deleteRole(@PathVariable String role) {
        try {
            roleService.deleteRole(role);
            return new ApiResponse<>(HttpStatus.OK.value(), "permission.delete.success", "Permission deleted successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "permission.delete.fail", e.getMessage());
        }
    }

}
