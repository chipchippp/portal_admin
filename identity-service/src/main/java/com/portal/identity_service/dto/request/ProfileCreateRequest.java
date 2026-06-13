package com.portal.identity_service.dto.request;

import com.portal.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreateRequest {
    String userId;
    String fullName;
    String city;
    LocalDate dateOfBirth;
}
