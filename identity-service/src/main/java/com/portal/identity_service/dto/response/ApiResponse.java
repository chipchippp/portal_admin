package com.portal.identity_service.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@JsonPropertyOrder({ "status", "message", "data" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse(int status, String message) {
    }
}
