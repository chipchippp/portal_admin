package com.portal.identity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionResponse {
    String id;
    String device;
    String ip;
    String browser;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime loginTime;
}
