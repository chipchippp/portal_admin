package com.protal.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String fullName;
    String email;
    //    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
    String city;
}
