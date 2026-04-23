package com.portal.identity_service.controller;

import com.portal.identity_service.dto.request.PermissionRequest;
import com.portal.identity_service.dto.response.ApiResponse;
import com.portal.identity_service.dto.response.PermissionResponse;
import com.portal.identity_service.service.PermissionService;
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
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> addPermission(@Valid @RequestBody PermissionRequest request){
            return ApiResponse.<PermissionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("permission.create.success")
                    .data(permissionService.createPermission(request))
                    .build();
    }


    @GetMapping("getAll")
    public ApiResponse<List<PermissionResponse>> getPermission(){
        try {
            List<PermissionResponse> permission = permissionService.getAllPermissions();
            return new ApiResponse<>(HttpStatus.OK.value(), "Get users successfully", permission);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "user.get.fail");
        }
    }


    @DeleteMapping("/{permission}")
    public ApiResponse<String> deleteUser(@PathVariable String permission) {
        try {
            permissionService.deletePermission(permission);
            return new ApiResponse<>(HttpStatus.OK.value(), "permission.delete.success", "Permission deleted successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "permission.delete.fail", e.getMessage());
        }
    }

}
