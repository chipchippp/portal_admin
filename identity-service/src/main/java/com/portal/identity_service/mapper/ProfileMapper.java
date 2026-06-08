package com.portal.identity_service.mapper;

import com.portal.identity_service.dto.request.ProfileCreateRequest;
import com.portal.identity_service.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreateRequest toProfileCreateRequest(UserCreateRequest userCreateRequest);
}
