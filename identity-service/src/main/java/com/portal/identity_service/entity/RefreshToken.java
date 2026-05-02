package com.portal.identity_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "login_time")
    LocalDateTime loginTime;
}
