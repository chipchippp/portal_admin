package com.portal.identity_service.dto.request;

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
     String password;
     String fullName;
     String email;
     String phoneNumber;
     String gender;
     String status;
     LocalDate dateOfBirth;
     List<String> roles;
}
