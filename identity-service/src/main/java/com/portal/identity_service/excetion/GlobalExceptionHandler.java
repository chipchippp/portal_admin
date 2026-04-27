package com.portal.identity_service.excetion;

import com.portal.identity_service.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setStatus(ErrorCode.UNCATEGORIZED_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handleRuntimeException(AppException ex) {
//        ex.printStackTrace();
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setStatus(ErrorCode.UNCATEGORIZED_ERROR.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = ex.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info("Constraint violation attributes: {}", attributes);

        } catch (IllegalArgumentException e) {
            // Use default error code
            e.getMessage();
        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .status(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
