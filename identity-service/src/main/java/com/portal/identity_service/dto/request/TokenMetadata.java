package com.portal.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenMetadata {
    String deviceId;
    String userAgent;
    String ipAddress;
}
