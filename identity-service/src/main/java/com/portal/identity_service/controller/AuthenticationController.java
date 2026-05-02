package com.portal.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.portal.identity_service.dto.request.AuthenticationRequest;
import com.portal.identity_service.dto.request.IntrospectRequest;
import com.portal.identity_service.dto.request.LogoutRequest;
import com.portal.identity_service.dto.request.RefreshRequest;
import com.portal.identity_service.dto.response.ApiResponse;
import com.portal.identity_service.dto.response.AuthenticationResponse;
import com.portal.identity_service.dto.response.IntrospectResponse;
import com.portal.identity_service.dto.response.SessionResponse;
import com.portal.identity_service.entity.RefreshToken;
import com.portal.identity_service.repository.RefreshTokenRepository;
import com.portal.identity_service.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletRequest httpRequest) throws ParseException, JOSEException {

        request.setIpAddress(httpRequest.getRemoteAddr());
        request.setUserAgent(httpRequest.getHeader("User-Agent"));

        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(200)
                .message("Authentication successful")
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .status(200)
                .message("Introspection successful")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {

        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .status(200)
                .message("Logout successful")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) throws ParseException, JOSEException {

        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(200)
                .message("Authentication successful")
                .data(result)
                .build();
    }

    @GetMapping("/sessions")
    public List<SessionResponse> getSessions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return authenticationService.getUserSessions(username);
    }

    @PostMapping("/logout-device")
    public void logoutDevice(@RequestBody Map<String, String> body) {
        String tokenId = body.get("refreshTokenId");
        authenticationService.logoutDevice(tokenId);
    }

    @PostMapping("/logout-all")
    public void logoutAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        authenticationService.logoutAllDevices(username);
    }
}
