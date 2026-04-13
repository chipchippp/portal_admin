package com.portal.identity_service.configuration;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/api/v1/user/**"
    };

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable);

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.decoder(jwtDecoder()))
        );
        return http.build();
    }

    // khi request đến sẽ được giải mã bằng JwtDecoder, nếu giải mã thành công thì sẽ được coi là đã xác thực
    // ví dụ như khi request token đến endpoint /api/v1/user/me, thì sẽ được giải mã bằng JwtDecoder,
    // nếu giải mã thành công thì sẽ được coi là đã xác thực và có thể truy cập vào endpoint đó
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

    }
}
