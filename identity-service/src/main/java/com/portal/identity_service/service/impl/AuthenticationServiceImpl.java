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
import com.portal.identity_service.repository.*;
import com.portal.identity_service.service.AuthenticationService;
import jakarta.transaction.Transactional;
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
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import static java.time.ZoneId.systemDefault;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
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

    // ========================================================
    // ===================== PUBLIC APIs =======================
    // ========================================================

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws JOSEException, ParseException {

        User user = getUser(request.getUsername());
        validatePassword(request, user);

        limitDevices(user);

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        saveRefreshToken(refreshToken, user, buildMetadata(request));

        return buildAuthResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {

        SignedJWT jwt = verifyToken(request.getToken(), "refresh");

        RefreshToken stored = getValidRefreshToken(jwt);

        // revoke old
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        User user = getUser(jwt.getJWTClaimsSet().getSubject());

        String newAccess = generateAccessToken(user);
        String newRefresh = generateRefreshToken(user);

        saveRefreshToken(newRefresh, user, cloneMetadata(stored));

        return buildAuthResponse(newAccess, newRefresh);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT jwt = verifyToken(request.getToken(), "refresh");

        RefreshToken stored = getValidRefreshToken(jwt);

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        log.info("User {} logged out", stored.getUsername());
    }

    @Override
    public void logoutDevice(String tokenId) {
        RefreshToken token = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public void logoutAllDevices(String username) {
        refreshTokenRepository.revokeAll(username);
    }

    @Override
    public List<SessionResponse> getUserSessions(String username) {
        return refreshTokenRepository
                .findByUsernameAndRevokedFalse(username)
                .stream()
                .map(this::mapToSession)
                .toList();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {

        boolean valid = true;

        try {
            verifyToken(request.getToken(), "access");
        } catch (Exception e) {
            valid = false;
        }

        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    // ========================================================
    // ================= BUSINESS LOGIC ========================
    // ========================================================

    private void limitDevices(User user) {
        int MAX_DEVICE = 5;

        List<RefreshToken> tokens =
                refreshTokenRepository.findByUsernameAndRevokedFalseOrderByExpiryTimeAsc(user.getUsername());

        if (tokens.size() >= MAX_DEVICE) {
            int removeCount = tokens.size() - MAX_DEVICE + 1;

            for (int i = 0; i < removeCount; i++) {
                tokens.get(i).setRevoked(true);
            }

            refreshTokenRepository.saveAll(tokens);
        }
    }

    // ========================================================
    // ================= TOKEN GENERATION ======================
    // ========================================================

    private String generateAccessToken(User user) throws JOSEException {
        return buildToken(user, VALID_DURATION, "access");
    }

    private String generateRefreshToken(User user) throws JOSEException {
        return buildToken(user, REFRESHABLE_DURATION, "refresh");
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

        JWSObject jws = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS512),
                new Payload(claims.toJSONObject())
        );

        jws.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jws.serialize();
    }

    // ========================================================
    // ================= TOKEN VERIFICATION ====================
    // ========================================================

    private SignedJWT verifyToken(String token, String expectedType)
            throws ParseException, JOSEException {

        SignedJWT jwt = SignedJWT.parse(token);

        verifySignature(jwt);
        verifyType(jwt, expectedType);
        verifyExpiration(jwt);

        if (isRefreshToken(jwt)) {
            verifyRefreshTokenInDB(jwt);
        }

        return jwt;
    }

    private void verifySignature(SignedJWT jwt) throws JOSEException {
        if (!jwt.verify(new MACVerifier(SIGNER_KEY.getBytes()))) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void verifyType(SignedJWT jwt, String expectedType) throws ParseException {
        String type = jwt.getJWTClaimsSet().getStringClaim("type");

        if (!expectedType.equals(type)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void verifyExpiration(SignedJWT jwt) throws ParseException {
        if (jwt.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void verifyRefreshTokenInDB(SignedJWT jwt) throws ParseException {
        String jti = jwt.getJWTClaimsSet().getJWTID();

        RefreshToken stored = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (stored.isRevoked() || stored.getExpiryTime().before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    // Lưu refresh token vào database để quản lý phiên đăng nhập và hỗ trợ chức năng logout, revoke token
    private void saveRefreshToken(String token, User user, TokenMetadata request) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(jwt.getJWTClaimsSet().getJWTID())
                .username(user.getUsername())
                .expiryTime(jwt.getJWTClaimsSet().getExpirationTime())
                .revoked(false)
                .deviceId(request.getDeviceId())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .loginTime(Instant.now().atZone(systemDefault()).toLocalDateTime())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    private boolean isRefreshToken(SignedJWT jwt) throws ParseException {
        return "refresh".equals(jwt.getJWTClaimsSet().getStringClaim("type"));
    }

    // ========================================================
    // ================= HELPER METHODS ========================
    // ========================================================

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private void validatePassword(AuthenticationRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private RefreshToken getValidRefreshToken(SignedJWT jwt) throws ParseException {
        String jti = jwt.getJWTClaimsSet().getJWTID();

        RefreshToken stored = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (stored.isRevoked() || stored.getExpiryTime().before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return stored;
    }

    private TokenMetadata buildMetadata(AuthenticationRequest request) {
        return TokenMetadata.builder()
                .deviceId(request.getDeviceId())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .build();
    }

    private TokenMetadata cloneMetadata(RefreshToken token) {
        return TokenMetadata.builder()
                .deviceId(token.getDeviceId())
                .ipAddress(token.getIpAddress())
                .userAgent(token.getUserAgent())
                .build();
    }

    private SessionResponse mapToSession(RefreshToken t) {
        return SessionResponse.builder()
                .id(t.getId())
                .device(t.getDeviceId())
                .ip(t.getIpAddress())
                .browser(parseBrowser(t.getUserAgent()))
                .loginTime(t.getLoginTime())
                .build();
    }

    private String parseBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        return "Other";
    }

    private AuthenticationResponse buildAuthResponse(String access, String refresh) {
        return AuthenticationResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .authenticated(true)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());

                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(p -> joiner.add(p.getName()));
                }
            });
        }

        return joiner.toString();
    }
}
