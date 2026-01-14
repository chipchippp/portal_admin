package com.portal.identity_service.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String status;
    private LocalDate dateOfBirth;
}
