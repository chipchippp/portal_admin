package com.protal.profile_service.mapper;

import com.protal.profile_service.dto.request.UserProfileCreateRequest;
import com.protal.profile_service.dto.request.UserProfileUpdateRequest;
import com.protal.profile_service.dto.response.UserProfileResponse;
import com.protal.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreateRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    List<UserProfileResponse> toUserProfileResponseList(List<UserProfile> entities);

    void updateUserProfileFromRequest(UserProfileUpdateRequest request, @MappingTarget UserProfile entity);
}
