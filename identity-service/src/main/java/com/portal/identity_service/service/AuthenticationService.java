package com.portal.identity_service.service;

import com.nimbusds.jose.JOSEException;
import com.portal.identity_service.dto.request.*;
import com.portal.identity_service.dto.response.*;
import java.text.ParseException;
import java.util.List;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException, ParseException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    void logoutAllDevices(String username);
    void logoutDevice(String tokenId);
    List<SessionResponse> getUserSessions(String username);
}
