package com.protal.profile_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileCreateRequest {
    @NotBlank
    String userId;

    @NotBlank
    String fullName;
    //    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
    String city;

}
