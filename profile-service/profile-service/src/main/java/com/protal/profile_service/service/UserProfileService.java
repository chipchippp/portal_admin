package com.protal.profile_service.service;

import com.protal.profile_service.dto.request.UserProfileCreateRequest;
import com.protal.profile_service.dto.response.UserProfileResponse;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse createProfile(UserProfileCreateRequest request);

    UserProfileResponse getUserProfile(String id);

}
