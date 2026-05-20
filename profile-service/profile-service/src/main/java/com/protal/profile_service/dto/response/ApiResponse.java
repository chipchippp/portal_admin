package com.protal.profile_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonPropertyOrder({"status", "message", "data"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    int status;
    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
