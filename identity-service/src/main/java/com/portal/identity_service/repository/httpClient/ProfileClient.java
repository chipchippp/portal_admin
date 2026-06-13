package com.portal.identity_service.repository.httpClient;

import com.portal.identity_service.dto.request.ProfileCreateRequest;
import com.portal.identity_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "profile-service", url = "${app.server.url}")
public interface ProfileClient {
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreateRequest request);

    @GetMapping("/users/{userId}")
    UserProfileResponse getProfile(@PathVariable String userId);
}
