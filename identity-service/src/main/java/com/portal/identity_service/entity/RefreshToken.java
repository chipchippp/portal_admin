package com.portal.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    String id; // jti
    String username;
    Date expiryTime;
    boolean revoked;
    String deviceId;
    String ipAddress;
    String userAgent;
}
