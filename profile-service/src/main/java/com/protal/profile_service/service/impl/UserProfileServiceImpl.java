package com.protal.profile_service.service.impl;

import com.protal.profile_service.dto.request.UserProfileCreateRequest;
import com.protal.profile_service.dto.response.UserProfileResponse;
import com.protal.profile_service.entity.UserProfile;
import com.protal.profile_service.mapper.UserProfileMapper;
import com.protal.profile_service.repository.UserProfileRepository;
import com.protal.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createProfile(UserProfileCreateRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getUserProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
