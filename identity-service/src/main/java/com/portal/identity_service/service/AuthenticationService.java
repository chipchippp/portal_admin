package com.portal.identity_service.service;

import com.nimbusds.jose.JOSEException;
import com.portal.identity_service.dto.request.AuthenticationRequest;
import com.portal.identity_service.dto.request.IntrospectRequest;
import com.portal.identity_service.dto.request.LogoutRequest;
import com.portal.identity_service.dto.request.RefreshRequest;
import com.portal.identity_service.dto.response.AuthenticationResponse;
import com.portal.identity_service.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
