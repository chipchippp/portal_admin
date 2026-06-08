package com.protal.profile_service.service.impl;

import com.protal.profile_service.dto.request.UserProfileCreateRequest;
import com.protal.profile_service.dto.request.UserProfileUpdateRequest;
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

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createProfile(UserProfileCreateRequest request) {

        if(userProfileRepository
                .findByUserId(request.getUserId())
                .isPresent()) {

            throw new RuntimeException(
                    "Profile already exists for user "
                            + request.getUserId());
        }

        UserProfile userProfile =
                userProfileMapper.toUserProfile(request);

        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getUserProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileMapper.toUserProfileResponseList(
                userProfileRepository.findAll()
        );
    }

    @Override
    public UserProfileResponse updateProfile(
            String id,
            UserProfileUpdateRequest request) {

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        userProfileMapper.updateUserProfileFromRequest(
                request,
                profile
        );

        profile = userProfileRepository.save(profile);

        return userProfileMapper.toUserProfileResponse(profile);
    }

    @Override
    public UserProfileResponse getUserProfileByUserId(String userId) {
        UserProfile profile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        return userProfileMapper.toUserProfileResponse(profile);
    }

    @Override
    public void deleteProfile(String profileId) {

        userProfileRepository.deleteById(profileId);
    }

}
