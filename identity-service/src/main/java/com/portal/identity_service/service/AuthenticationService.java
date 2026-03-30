package com.portal.identity_service.service;

import com.portal.identity_service.dto.request.AuthenticationRequest;
import com.portal.identity_service.repository.UserRepository;

public interface AuthenticationService {
    boolean authenticate(AuthenticationRequest request);
}
