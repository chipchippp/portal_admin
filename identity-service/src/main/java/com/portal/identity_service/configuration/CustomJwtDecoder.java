package com.portal.identity_service.configuration;

import com.nimbusds.jose.JOSEException;
import com.portal.identity_service.dto.request.IntrospectRequest;
import com.portal.identity_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CustomJwtDecoder implements JwtDecoder {
    // khi request đến sẽ được giải mã bằng JwtDecoder, nếu giải mã thành công thì sẽ được coi là đã xác thực
    //    ví dụ như khi request token đến endpoint /api/v1/user/me, thì sẽ được giải mã bằng JwtDecoder,
    //    nếu giải mã thành công thì sẽ được coi là đã xác thực và có thể truy cập vào endpoint đó
    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    private final AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) {
                throw new JwtException("Token is invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        }

        return nimbusJwtDecoder.decode(token);
    }

}
