package com.portal.identity_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.portal.identity_service.dto.request.*;
import com.portal.identity_service.dto.response.*;
import com.portal.identity_service.entity.RefreshToken;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.excetion.*;
import com.portal.identity_service.repository.InvalidatedTokenRepository;
import com.portal.identity_service.repository.RefreshTokenRepository;
import com.portal.identity_service.repository.UserRepository;
import com.portal.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException, ParseException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        saveRefreshToken(refreshToken, user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, "access");
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        SignedJWT jwt = verifyToken(request.getToken(), "refresh");

        String jti = jwt.getJWTClaimsSet().getJWTID();
        String username = jwt.getJWTClaimsSet().getSubject();

        RefreshToken stored = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (stored.isRevoked()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        log.info("User {} logged out successfully", username);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {

        SignedJWT jwt = verifyToken(request.getToken(), "refresh");

        String jti = jwt.getJWTClaimsSet().getJWTID();

        RefreshToken stored = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (stored.isRevoked() || stored.getExpiryTime().before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // 🔥 revoke token cũ
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        String username = jwt.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);

        saveRefreshToken(newRefreshToken, user);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .authenticated(true)
                .build();
    }


    private String buildToken(User user, long duration, String type) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("loc.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("type", type)
                .claim("scope", buildScope(user))
                .build();

        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS512),
                new Payload(claims.toJSONObject())
        );

        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, String expectedType)
            throws ParseException, JOSEException {

        SignedJWT jwt = SignedJWT.parse(token);

        if (!jwt.verify(new MACVerifier(SIGNER_KEY.getBytes()))) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        var claims = jwt.getJWTClaimsSet();

        if (!expectedType.equals(claims.getStringClaim("type"))) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (claims.getExpirationTime().before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return jwt;
    }

    private void saveRefreshToken(String token, User user) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(jwt.getJWTClaimsSet().getJWTID())
                .username(user.getUsername())
                .expiryTime(jwt.getJWTClaimsSet().getExpirationTime())
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    private String generateRefreshToken(User user) throws JOSEException {
        return buildToken(user, REFRESHABLE_DURATION, "refresh");
    }


    private String generateAccessToken(User user) throws JOSEException {
        return buildToken(user, VALID_DURATION, "access");
    }
}
