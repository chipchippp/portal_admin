package com.portal.identity_service.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonPropertyOrder({ "status", "message", "data" })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T>{
     int status;
     String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
     T data;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
