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
public class UserCreateRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String fullName;
    String email;
    @Size(min = 10, message = "PHONE_INVALID")
    String phoneNumber;
    String gender;
    String status;

    @DobConstraint(minAge = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
}
