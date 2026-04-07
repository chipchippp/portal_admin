package com.portal.identity_service.excetion;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED_ERROR(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Invalid message key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters long"),
    PASSWORD_INVALID(1004, "Password must be at least 6 characters long"),
    PHONE_INVALID(1005, "Phone number must be at least 10 characters long"),
    USER_NOT_EXISTED(1006, "User not existed"),
    UNAUTHENTICATED(1007, "Unauthenticated"),

    ;
    private int code;
    private String message;
}
