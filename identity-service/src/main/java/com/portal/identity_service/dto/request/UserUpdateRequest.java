package com.portal.identity_service.dto.request;

import com.portal.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     @Size(min = 6, message = "PASSWORD_INVALID")
     String password;
     String fullName;
     String email;
     @Size(min = 10, message = "PHONE_INVALID")
     String phoneNumber;
     String gender;
     String status;
     @DobConstraint(min = 18, message = "INVALID_DOB")
     LocalDate dateOfBirth;
     List<String> roles;
}
