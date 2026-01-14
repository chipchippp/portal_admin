package com.portal.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequest {
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String status;
    private LocalDate dateOfBirth;
}
