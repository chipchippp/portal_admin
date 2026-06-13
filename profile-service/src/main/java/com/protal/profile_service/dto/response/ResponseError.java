package com.protal.profile_service.dto.response;

public class ResponseError extends ApiResponse<Void> {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
