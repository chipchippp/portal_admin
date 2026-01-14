package com.portal.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String status;
    private LocalDate dateOfBirth;
}
