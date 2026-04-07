package com.portal.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.portal.identity_service.dto.request.AuthenticationRequest;
import com.portal.identity_service.dto.request.IntrospectRequest;
import com.portal.identity_service.dto.response.ApiResponse;
import com.portal.identity_service.dto.response.AuthenticationResponse;
import com.portal.identity_service.dto.response.IntrospectResponse;
import com.portal.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

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
}
