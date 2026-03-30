package com.portal.identity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portal.identity_service.enums.Gender;
import com.portal.identity_service.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
      Long id;
      String username;
      String fullName;
      String email;
      String phoneNumber;
      @JsonIgnore
      String password;
      Gender gender;
      Status status;
      @JsonFormat(pattern = "yyyy-MM-dd")
      LocalDate dateOfBirth;

}
