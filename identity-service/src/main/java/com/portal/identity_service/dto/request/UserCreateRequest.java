package com.portal.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
    private String fullName;
    private String email;
    @Size(min = 10, message = "PHONE_INVALID")
    private String phoneNumber;
    private String gender;
    private String status;
    private LocalDate dateOfBirth;
}
