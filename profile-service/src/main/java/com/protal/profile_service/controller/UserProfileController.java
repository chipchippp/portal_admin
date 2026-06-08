package com.protal.profile_service.controller;

import com.protal.profile_service.dto.request.*;
import com.protal.profile_service.dto.response.UserProfileResponse;
import com.protal.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/create")
    public UserProfileResponse createUserProfile(@RequestBody UserProfileCreateRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{profileId}")
    public UserProfileResponse getUserProfile(@PathVariable String profileId) {
        return userProfileService.getUserProfile(profileId);
    }

    @GetMapping()
    public List<UserProfileResponse> getAllUserProfiles() {
        return userProfileService.getAllProfiles();
    }

    @PutMapping("/{profileId}")
    public UserProfileResponse updateUserProfile(
            @PathVariable String profileId,
            @RequestBody UserProfileUpdateRequest request) {

        return userProfileService.updateProfile(profileId, request);
    }

    @GetMapping("/users/{userId}")
    public UserProfileResponse getProfileByUserId(
            @PathVariable String userId) {

        return userProfileService.getUserProfileByUserId(userId);
    }

    @DeleteMapping("/{profileId}")
    public void deleteProfile(
            @PathVariable String profileId) {

        userProfileService.deleteProfile(profileId);
    }

}
